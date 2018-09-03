package com.level500.ub.zebracomics;

public class Comics {
        private String name;
        private String numOfComics;
        private String thumbnail;
        private String comicId;

        public Comics() {
        }

        public Comics(String name, String numOfComics, String thumbnail, String comicId) {
            this.name = name;
            this.numOfComics = numOfComics;
            this.thumbnail = thumbnail;
            this.comicId = comicId;
        }

        public String getName() {
            return name;
        }

        public String getComicId() {return comicId; }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumOfComics() {
            return numOfComics;
        }

        public void setNumOfComics(String numOfComics) {
            this.numOfComics = numOfComics;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }
}
