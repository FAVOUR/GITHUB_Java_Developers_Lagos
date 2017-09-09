package com.example.android.lagos_java_developers.model;


    public class DeveloperProfile {

        private  String developersName;
        private  String bio;
        private  String pfollowers;
        private  String pfollowing;
        private  String plocation;
        private  String pemail;
        private  String phireable;
        private  String pRepos;

        public DeveloperProfile(String developersName, String bio, String repos, String followers, String following, String location, String email, String hireable ){



            this.developersName = developersName;
            this.bio = bio;
            pRepos           = repos;
            pfollowers       = followers;
            pfollowing       = following;
            plocation        = location;
            pemail           = email;
            phireable        = hireable;




        }

        public String getDevelopersName() {
            return developersName;
        }

        public String getBio() {
            return bio;
        }

        public String getpRepos() {
            return pRepos;
        }

        public String getPFollowers() {
            return pfollowers;
        }

        public String getPFollowing() {
            return pfollowing;
        }

        public String getPLocation() {
            return plocation;
        }

        public String getPEmail() {
            return pemail;
        }

        public String getPHireable() {
            return phireable;
        }


    }
