package contenttypes;

public class Tag {
    private String name;
    private int tagID;

    public Tag(String name, int id) {
        this.name = name;
        this.tagID = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTagID() {
        return tagID;
    }
}