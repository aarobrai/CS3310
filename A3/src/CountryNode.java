/**
 * PROJECT: A3  CLASS: CountryNode
 * AUTHOR: aaronbrainard
 *
 *
 * FILES ACCESSED: n/a
 *
 *
 *
 *
 * DESCRIPTION: Provides data type and constructor for the nodes inserted
 * into the hash table
 */
public class CountryNode {

    private String code;
    private int dataRecordPointer;
    private int link;

    public CountryNode(String code, int dataRecordPointer, int link){
        this.code = code;
        this.dataRecordPointer = dataRecordPointer;
        this.link = link;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getDataRecordPointer() {
        return dataRecordPointer;
    }

    public void setDataRecordPointer(int dataRecordPointer) {
        this.dataRecordPointer = dataRecordPointer;
    }

    public int getLink() {
        return link;
    }

    public void setLink(int link) {
        this.link = link;
    }
}
