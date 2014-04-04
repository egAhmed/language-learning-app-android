package com.eavg.sampleapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Eric on 3/25/14.
 */
public class Item {

    private int id;
    private String uri;
    private Cue cue;
    private Response response;

    public int getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public String getType() {
        return cue.type;
    }

    public String getText() {
        return cue.content.text;
    }

    public String getSoundPath() {
        return cue.content.sound;
    }

    public String getPartOfSpeech() {
        return cue.related.partOfSpeech;
    }

    public String getTransliteration() {
        String transliterationText = "";
        if (cue.related.transliterations != null) {
            for (Transliteration transliteration : cue.related.transliterations) {
                if (transliteration.type.equalsIgnoreCase("hira")) {
                    transliterationText = transliteration.text;
                    break;
                }
            }
        }
        return transliterationText;
    }

    public String getMeaning() {
        if (response.type.equalsIgnoreCase("meaning")) {
            return response.content.text;
        }
        return "";
    }

    static class Cue {
        private String type;
        private Content content;
        private Related related;

        static class Related {
            private String language;
            @SerializedName("part_of_speech")
            private String partOfSpeech;
            private Transliteration[] transliterations;
        }
    }
    static class Content {
        private String text;
        private String sound;
    }

    static class Transliteration {
        private String type;
        private String text;
    }

    static class Response {
        private String type;
        private Content content;
    }

    @Override
    public String toString() {
        return String.format("Item id: %1$s uri: %2$s text: %3$s", id, uri, cue.content.text);
    }
}
