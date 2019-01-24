package com.lollykrown.rccgdc.model;


public class Video {
    String VideoName;
    String VideoDesc;
    String URL;
    String VideoId;
    String ChannelTitle;


public void setVideoName(String VideoName){
    this.VideoName=VideoName;
}

public String getVideoName(){
    return VideoName;
}

public void setVideoDesc(String VideoDesc){
    this.VideoDesc=VideoDesc;
}

public String getVideoDesc(){
    return VideoDesc;
}

public void setChannelTitle(String ChannelTitle){
        this.ChannelTitle=ChannelTitle;
    }

public String getChannelTitle(){
        return ChannelTitle;
    }

public void setURL(String URL){
    this.URL=URL;
}

public String getURL(){
    return URL;
}

public void setVideoId(String VideoId){
    this.VideoId=VideoId;
}
public String getVideoId(){
    return VideoId;
}

}
