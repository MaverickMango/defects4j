package org.jsoup.nodes;


public class DocumentType extends org.jsoup.nodes.Node {
    public DocumentType(java.lang.String name, java.lang.String publicId, java.lang.String systemId, java.lang.String baseUri) {
        super(baseUri);
        attr("name", name);
        attr("publicId", publicId);
        attr("systemId", systemId);
    }

    @java.lang.Override
    public java.lang.String nodeName() {
        return "#doctype";
    }

    @java.lang.Override
    void outerHtmlHead(java.lang.StringBuilder accum, int depth, org.jsoup.nodes.Document.OutputSettings out) {
        accum.append("<!DOCTYPE");
        if (!(org.jsoup.helper.StringUtil.isBlank(attr("name"))))
            accum.append(" ").append(attr("name"));

        if (!(org.jsoup.helper.StringUtil.isBlank(attr("publicId"))))
            accum.append(" PUBLIC \"").append(attr("publicId")).append('"');

        if (!(org.jsoup.helper.StringUtil.isBlank(attr("systemId"))))
            accum.append(" \"").append(attr("systemId")).append('"');

        accum.append('>');
    }

    @java.lang.Override
    void outerHtmlTail(java.lang.StringBuilder accum, int depth, org.jsoup.nodes.Document.OutputSettings out) {
    }
}

