package org.jsoup.parser;


public class XmlTreeBuilder extends org.jsoup.parser.TreeBuilder {
    org.jsoup.parser.ParseSettings defaultSettings() {
        return ParseSettings.preserveCase;
    }

    org.jsoup.parser.Document parse(java.io.Reader input, java.lang.String baseUri) {
        return parse(input, baseUri, org.jsoup.parser.ParseErrorList.noTracking(), ParseSettings.preserveCase);
    }

    org.jsoup.parser.Document parse(java.lang.String input, java.lang.String baseUri) {
        return parse(new java.io.StringReader(input), baseUri, org.jsoup.parser.ParseErrorList.noTracking(), ParseSettings.preserveCase);
    }

    @java.lang.Override
    protected void initialiseParse(java.io.Reader input, java.lang.String baseUri, org.jsoup.parser.ParseErrorList errors, org.jsoup.parser.ParseSettings settings) {
        super.initialiseParse(input, baseUri, errors, settings);
        stack.add(doc);
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
    }

    @java.lang.Override
    protected boolean process(org.jsoup.parser.Token token) {
        switch (token.type) {
            case StartTag :
                insert(token.asStartTag());
                break;
            case EndTag :
                popStackToClose(token.asEndTag());
                break;
            case Comment :
                insert(token.asComment());
                break;
            case Character :
                insert(token.asCharacter());
                break;
            case Doctype :
                insert(token.asDoctype());
                break;
            case EOF :
                break;
            default :
                org.jsoup.helper.Validate.fail(("Unexpected token type: " + (token.type)));
        }
        return true;
    }

    private void insertNode(org.jsoup.parser.Node node) {
        currentElement().appendChild(node);
    }

    org.jsoup.parser.Element insert(org.jsoup.parser.Token.StartTag startTag) {
        org.jsoup.parser.Tag tag = org.jsoup.parser.Tag.valueOf(startTag.name(), settings);
        org.jsoup.parser.Element el = new org.jsoup.parser.Element(tag, baseUri, settings.normalizeAttributes(startTag.attributes));
        insertNode(el);
        if (startTag.isSelfClosing()) {
            if (!(tag.isKnownTag()))
                tag.setSelfClosing();

        }else {
            stack.add(el);
        }
        return el;
    }

    void insert(org.jsoup.parser.Token.Comment commentToken) {
        org.jsoup.parser.Comment comment = new org.jsoup.parser.Comment(commentToken.getData());
        org.jsoup.parser.Node insert = comment;
        if (commentToken.bogus) {
            java.lang.String data = comment.getData();
            if (((data.length()) > 1) && ((data.startsWith("!")) || (data.startsWith("?")))) {
                org.jsoup.parser.Document doc = org.jsoup.Jsoup.parse((("<" + (data.substring(1, ((data.length()) - 1)))) + ">"), baseUri, org.jsoup.parser.Parser.xmlParser());
                org.jsoup.parser.Element el = doc.child(0);
                insert = new org.jsoup.parser.XmlDeclaration(settings.normalizeTag(el.tagName()), data.startsWith("!"));
                insert.attributes().addAll(el.attributes());
            }
        }
        insertNode(insert);
    }

    void insert(org.jsoup.parser.Token.Character token) {
        final java.lang.String data = token.getData();
        insertNode((token.isCData() ? new org.jsoup.parser.CDataNode(data) : new org.jsoup.parser.TextNode(data)));
    }

    void insert(org.jsoup.parser.Token.Doctype d) {
        org.jsoup.parser.DocumentType doctypeNode = new org.jsoup.parser.DocumentType(settings.normalizeTag(d.getName()), d.getPublicIdentifier(), d.getSystemIdentifier());
        doctypeNode.setPubSysKey(d.getPubSysKey());
        insertNode(doctypeNode);
    }

    private void popStackToClose(org.jsoup.parser.Token.EndTag endTag) {
        java.lang.String elName = endTag.normalName();
        org.jsoup.parser.Element firstFound = null;
        for (int pos = (stack.size()) - 1; pos >= 0; pos--) {
            org.jsoup.parser.Element next = stack.get(pos);
            if (next.nodeName().equals(elName)) {
                firstFound = next;
                break;
            }
        }
        if (firstFound == null)
            return;

        for (int pos = (stack.size()) - 1; pos >= 0; pos--) {
            org.jsoup.parser.Element next = stack.get(pos);
            stack.remove(pos);
            if (next == firstFound)
                break;

        }
    }

    java.util.List<org.jsoup.parser.Node> parseFragment(java.lang.String inputFragment, java.lang.String baseUri, org.jsoup.parser.ParseErrorList errors, org.jsoup.parser.ParseSettings settings) {
        initialiseParse(new java.io.StringReader(inputFragment), baseUri, errors, settings);
        runParser();
        return doc.childNodes();
    }
}

