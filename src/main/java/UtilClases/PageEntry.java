package UtilClases;

public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final Integer page;
    private final Integer count;

    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }

    public String getPdfName() {
        return pdfName;
    }

    public int getPage() {
        return page;
    }

    public Integer getCount() {
        return count;
    }

    @Override
    public int compareTo(PageEntry o1) {
        int result = this.pdfName.compareTo(o1.pdfName);

        if (result == 0) {
            result = o1.count.compareTo(this.count);
        }

        if (result == 0) {
            result = this.page.compareTo(o1.page);
        }

        return result;
    }

    @Override
    public String toString() {
        return "PageEntry{" +
                "pdfName='" + pdfName + '\'' +
                ", page=" + page +
                ", count=" + count +
                '}' + '\n';
    }
}
