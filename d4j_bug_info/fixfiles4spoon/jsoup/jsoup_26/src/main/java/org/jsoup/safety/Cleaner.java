package org.jsoup.safety;


public class Cleaner {
    private org.jsoup.safety.Whitelist whitelist;

    public Cleaner(org.jsoup.safety.Whitelist whitelist) {
        org.jsoup.helper.Validate.notNull(whitelist);
        this.whitelist = whitelist;
    }

    public org.jsoup.safety.Document clean(org.jsoup.safety.Document dirtyDocument) {
        org.jsoup.helper.Validate.notNull(dirtyDocument);
        org.jsoup.safety.Document clean = org.jsoup.safety.Document.createShell(dirtyDocument.baseUri());
        if ((dirtyDocument.body()) != null)
            copySafeNodes(dirtyDocument.body(), clean.body());

        return clean;
    }

    public boolean isValid(org.jsoup.safety.Document dirtyDocument) {
        org.jsoup.helper.Validate.notNull(dirtyDocument);
        org.jsoup.safety.Document clean = org.jsoup.safety.Document.createShell(dirtyDocument.baseUri());
        int numDiscarded = copySafeNodes(dirtyDocument.body(), clean.body());
        return numDiscarded == 0;
    }

    private int copySafeNodes(org.jsoup.safety.Element source, org.jsoup.safety.Element dest) {
        java.util.List<org.jsoup.safety.Node> sourceChildren = source.childNodes();
        int numDiscarded = 0;
        for (org.jsoup.safety.Node sourceChild : sourceChildren) {
            if (sourceChild instanceof org.jsoup.safety.Element) {
                org.jsoup.safety.Element sourceEl = ((org.jsoup.safety.Element) (sourceChild));
                if (whitelist.isSafeTag(sourceEl.tagName())) {
                    org.jsoup.safety.Cleaner.ElementMeta meta = createSafeElement(sourceEl);
                    org.jsoup.safety.Element destChild = meta.el;
                    dest.appendChild(destChild);
                    numDiscarded += meta.numAttribsDiscarded;
                    numDiscarded += copySafeNodes(sourceEl, destChild);
                }else {
                    numDiscarded++;
                    numDiscarded += copySafeNodes(sourceEl, dest);
                }
            }else
                if (sourceChild instanceof org.jsoup.safety.TextNode) {
                    org.jsoup.safety.TextNode sourceText = ((org.jsoup.safety.TextNode) (sourceChild));
                    org.jsoup.safety.TextNode destText = new org.jsoup.safety.TextNode(sourceText.getWholeText(), sourceChild.baseUri());
                    dest.appendChild(destText);
                }

        }
        return numDiscarded;
    }

    private org.jsoup.safety.Cleaner.ElementMeta createSafeElement(org.jsoup.safety.Element sourceEl) {
        java.lang.String sourceTag = sourceEl.tagName();
        org.jsoup.safety.Attributes destAttrs = new org.jsoup.safety.Attributes();
        org.jsoup.safety.Element dest = new org.jsoup.safety.Element(org.jsoup.parser.Tag.valueOf(sourceTag), sourceEl.baseUri(), destAttrs);
        int numDiscarded = 0;
        org.jsoup.safety.Attributes sourceAttrs = sourceEl.attributes();
        for (org.jsoup.safety.Attribute sourceAttr : sourceAttrs) {
            if (whitelist.isSafeAttribute(sourceTag, sourceEl, sourceAttr))
                destAttrs.put(sourceAttr);
            else
                numDiscarded++;

        }
        org.jsoup.safety.Attributes enforcedAttrs = whitelist.getEnforcedAttributes(sourceTag);
        destAttrs.addAll(enforcedAttrs);
        return new org.jsoup.safety.Cleaner.ElementMeta(dest, numDiscarded);
    }

    private static class ElementMeta {
        org.jsoup.safety.Element el;

        int numAttribsDiscarded;

        ElementMeta(org.jsoup.safety.Element el, int numAttribsDiscarded) {
            this.el = el;
            this.numAttribsDiscarded = numAttribsDiscarded;
        }
    }
}

