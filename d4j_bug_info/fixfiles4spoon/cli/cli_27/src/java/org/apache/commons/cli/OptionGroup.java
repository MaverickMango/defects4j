package org.apache.commons.cli;


public class OptionGroup implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private java.util.Map optionMap = new java.util.HashMap();

    private java.lang.String selected;

    private boolean required;

    public org.apache.commons.cli.OptionGroup addOption(org.apache.commons.cli.Option option) {
        optionMap.put(option.getKey(), option);
        return this;
    }

    public java.util.Collection getNames() {
        return optionMap.keySet();
    }

    public java.util.Collection getOptions() {
        return optionMap.values();
    }

    public void setSelected(org.apache.commons.cli.Option option) throws org.apache.commons.cli.AlreadySelectedException {
        if (option == null) {
            selected = null;
            return;
        }
        if (((selected) == null) || (selected.equals(option.getKey()))) {
            selected = option.getKey();
        }else {
            throw new org.apache.commons.cli.AlreadySelectedException(this, option);
        }
    }

    public java.lang.String getSelected() {
        return selected;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isRequired() {
        return required;
    }

    public java.lang.String toString() {
        java.lang.StringBuffer buff = new java.lang.StringBuffer();
        java.util.Iterator iter = getOptions().iterator();
        buff.append("[");
        while (iter.hasNext()) {
            org.apache.commons.cli.Option option = ((org.apache.commons.cli.Option) (iter.next()));
            if ((option.getOpt()) != null) {
                buff.append("-");
                buff.append(option.getOpt());
            }else {
                buff.append("--");
                buff.append(option.getLongOpt());
            }
            buff.append(" ");
            buff.append(option.getDescription());
            if (iter.hasNext()) {
                buff.append(", ");
            }
        } 
        buff.append("]");
        return buff.toString();
    }
}

