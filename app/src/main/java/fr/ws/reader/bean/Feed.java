

package fr.ws.reader.bean;


public class Souscription {

    private String title;
    private String link;

     public Souscription(String title,String link){
         setLink(link);
         setTitle(title);
     }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
         this.title=title;
    }


    public String getLink() {
        return link;
    }
    public void setLink(String Link) {
        this.link=Link;
    }


    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                '}';
    }



    public static final String NAME = Souscription.class.getSimpleName().toLowerCase();
    public static final String _ID = "_id";
    public static final String ADDRESS_NAME = "address_name";
    public static final String ADDRESS = "address";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + NAME +
                    " ( " +
                    _ID + " integer primary key autoincrement, " +
                    ADDRESS + " string unique," +
                    ADDRESS_NAME + " string " +
                    " ); ";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + NAME;
    public static String[] Columns = new String[]{_ID,ADDRESS_NAME,ADDRESS};
}