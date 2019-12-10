package fr.ws.reader.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 产品 数据体
 */
public class Feed implements Serializable {

    private Integer entity_id=0;
    private String title;
    private String link;

    public Feed(Integer entity_id,String title,String link){

        this.entity_id=entity_id;
        this.title=title;
        this.link=link;
    }
    public Feed(){

    }
    public Integer getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(Integer entity_id) {
        this.entity_id = entity_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "entity_id='" + entity_id + '\'' +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +


                '}';
    }
}
