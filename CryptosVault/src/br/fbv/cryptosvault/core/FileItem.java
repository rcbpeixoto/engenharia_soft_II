package br.fbv.cryptosvault.core;

import java.io.File;

/**
 * @author    Rogério
 */
public class FileItem implements Comparable<FileItem> {

    private File    file;
    /**
	 * @uml.property  name="backToParent"
	 */
    private boolean backToParent = false;
    /**
	 * @uml.property  name="selected"
	 */
    private boolean selected     = false;

    public FileItem(File file, boolean backToParent, boolean selected) {
        this(file, backToParent);
        this.selected = selected;
    }

    public FileItem(File file, boolean backToParent) {
        super();
        this.file = file;
        this.backToParent = backToParent;
    }

    public FileItem(File file) {
        super();
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    /**
	 * @return
	 * @uml.property  name="backToParent"
	 */
    public boolean isBackToParent() {
        return backToParent;
    }

    /**
	 * @return
	 * @uml.property  name="selected"
	 */
    public boolean isSelected() {
        return selected;
    }

    /**
	 * @param selected
	 * @uml.property  name="selected"
	 */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int compareTo(FileItem another) {
        return file.getName().compareTo(another.getFile().getName());
    }

}
