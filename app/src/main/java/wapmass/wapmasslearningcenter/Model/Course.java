package wapmass.wapmasslearningcenter.Model;

public class Course {
    private String cTitle;
    private String cDesc;
    private String imageLink;

    public String getcTitle() {
        return cTitle;
    }

    public void setcTitle(String cTitle) {
        this.cTitle = cTitle;
    }

    public String getcDesc() {
        return cDesc;
    }

    public void setcDesc(String cDesc) {
        this.cDesc = cDesc;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    @Override
    public String toString() {
        return "Course{" +
                "cTitle='" + cTitle + '\'' +
                ", cDesc='" + cDesc + '\'' +
                ", imageLink='" + imageLink + '\'' +
                '}';
    }
}
