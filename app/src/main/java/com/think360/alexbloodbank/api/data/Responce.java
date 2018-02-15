package com.think360.alexbloodbank.api.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by think360 on 09/10/17.
 */

public class Responce {

public class RegistrationResponce {
    public int getStatus() {
        return status;
    }

    @SerializedName("status")
    @Expose
    private int status;

    public String getMessage() {
        return message;
    }

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("description")
    @Expose
    private String description;
    class Data{
    public String getUser_id() {
        return user_id;
    }

    @SerializedName("user_id")
    @Expose
    private String user_id;
    }

}
    public class LoginResponce {

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public Data getData() {
            return data;
        }

        @SerializedName("status")
        @Expose
        private int status;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("data")
        @Expose
        private Data data;

       public class Data{

            public String getUserId() {return user_id;}

            public String getName() {
                return name;
            }

            public String getEmail() {
                return email;
            }

            public String getProfile_pic() {
                return profile_pic;
            }

            @SerializedName("user_id")
            @Expose
            private String user_id;

            @SerializedName("name")
            @Expose
            private String name;

            @SerializedName("email")
            @Expose
            private String email;

            @SerializedName("profile_pic")
            @Expose
            private String profile_pic;



        }

    }
    public class CountryResponce {
        @SerializedName("status")
        @Expose
        private int status;
        @SerializedName("message")
        @Expose
        private String message;
        @Expose
        private Data data;
        @SerializedName("data")

        public int getStatus() {
            return status;
        }
        public String getMessage() {
            return message;
        }
        public Data getData() {
            return data;
        }
       public class Data{
            @SerializedName("school")
            @Expose
            private List<School> schoolList;
            public List<School> getSchoolList() {
                return schoolList;
            }
          public   class School{
                @SerializedName("id")
                @Expose
                private String id;
                @SerializedName("name")
                @Expose
                private String name;
                public String getId() {
                    return id;
                }

                public String getName() {
                    return name;
                }
            }

        }

    }
    public class AddDonationResponce {

        @SerializedName("status")
        @Expose
        private int status;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("data")
        @Expose
        private Data data;


        public int getStatus() {return status;}
        public String getMessage() {
            return message;
        }
        public Data getData() {
            return data;
        }

        public class Data{
             @SerializedName("Dpost_id")
             @Expose
             private String Dpost_id;

             public String getDpostId() {
                 return Dpost_id;
             }


         }

    }
    public class AddVolunteerResponce {

        @SerializedName("status")
        @Expose
        private int status;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("data")
        @Expose
        private Data data;


        public int getStatus() {return status;}
        public String getMessage() {
            return message;
        }
        public Data getData() {
            return data;
        }

        public class Data{
            @SerializedName("Vpost_id")
            @Expose
            private String Vpost_id;

            public String getDpostId() {
                return Vpost_id;
            }
        }
    }
    public class RecruitResponce {

        @SerializedName("status")
        @Expose
        private int status;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("data")
        @Expose
        private Data data;


        public int getStatus() {return status;}
        public String getMessage() {
            return message;
        }
        public Data getData() {
            return data;
        }

        public class Data{
            @SerializedName("Rpost_id")
            @Expose
            private String Rpost_id;

            public String getDpostId() {
                return Rpost_id;
            }


        }

    }
    public class BloodResponce {

        @SerializedName("status")
        @Expose
        private int status;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("data")
        @Expose
        private Data data;


        public int getStatus() {return status;}
        public String getMessage() {
            return message;
        }
        public Data getData() {
            return data;
        }

        public class Data{
            @SerializedName("vpost_id")
            @Expose
            private String vpost_id;

            public String getDpostId() {
                return vpost_id;
            }


        }

    }
    public class HostListResponce {

        @SerializedName("status")
        @Expose
        private int status;

        @SerializedName("message")
        @Expose
        private String message;


        @SerializedName("data")
        @Expose
        private ArrayList<Data> data;


        public int getStatus() {return status;}
        public String getMessage() {
            return message;
        }
        public ArrayList<Data> getHostList() {return data;}

        public class Data{
            public String getId() {
                return id;
            }

            public String getUser_id() {
                return user_id;
            }

            public String getUserinfo() {
                return userinfo;
            }

            public String getRecuruiter_name() {
                return recuruiter_name;
            }

            public String getUserpostdate() {
                return userpostdate;
            }

            public String getPoststatus() {
                return poststatus;
            }

            @SerializedName("id")
            @Expose
            private String id;

            @SerializedName("user_id")
            @Expose
            private String user_id;

            @SerializedName("userinfo")
            @Expose
            private String userinfo;

            @SerializedName("recuruiter_name")
            @Expose
            private String recuruiter_name;

            @SerializedName("userpostdate")
            @Expose
            private String userpostdate;

            @SerializedName("poststatus")
            @Expose
            private String poststatus;
            public String getShare_url() {
                return share_url;
            }

            @SerializedName("share_url")
            @Expose
            private String share_url;
        }

    }
    public class DonorsListResponce {

        @SerializedName("status")
        @Expose
        private int status;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("data")
        @Expose
        private ArrayList<Data> data;


        public int getStatus() {return status;}
        public String getMessage() {
            return message;
        }
        public ArrayList<Data> getDonorsList() {return data;}

        public class Data{
            public String getId() {
                return id;
            }

            public String getUser_id() {
                return user_id;
            }

            public String getUserinfo() {
                return userinfo;
            }

            public String getUserpostdate() {
                return userpostdate;
            }

            public String getPoststatus() {
                return poststatus;
            }

            @SerializedName("id")
            @Expose
            private String id;

            @SerializedName("user_id")
            @Expose
            private String user_id;

            @SerializedName("userinfo")
            @Expose
            private String userinfo;

            @SerializedName("userpostdate")
            @Expose
            private String userpostdate;

            @SerializedName("poststatus")
            @Expose
            private String poststatus;

            public String getShare_url() {
                return share_url;
            }

            @SerializedName("share_url")
            @Expose
            private String share_url;
        }

    }
    public class RecruitListResponce {

        @SerializedName("status")
        @Expose
        private int status;

        @SerializedName("message")
        @Expose
        private String message;


        @SerializedName("data")
        @Expose
        private ArrayList<Data> data;


        public int getStatus() {return status;}
        public String getMessage() {
            return message;
        }
        public ArrayList<Data> getRecruitList() {return data;}

        public class Data{
            public String getId() {
                return id;
            }

            public String getUser_id() {
                return user_id;
            }

            public String getUserinfo() {
                return userinfo;
            }

            public String getPoststatus() {
                return poststatus;
            }

            @SerializedName("id")
            @Expose
            private String id;

            @SerializedName("user_id")
            @Expose
            private String user_id;

            @SerializedName("userinfo")
            @Expose
            private String userinfo;

            @SerializedName("poststatus")
            @Expose
            private String poststatus;

            public String getShare_url() {
                return share_url;
            }

            @SerializedName("share_url")
            @Expose
            private String share_url;

        }
    }
    public class NotificationListResponce {

        @SerializedName("status")
        @Expose
        private int status;

        @SerializedName("message")
        @Expose
        private String message;


        @SerializedName("data")
        @Expose
        private ArrayList<Data> data;


        public int getStatus() {return status;}
        public String getMessage() {
            return message;
        }
        public ArrayList<Data> getNotificationList() {return data;}

        public class Data{

            public String getUser_id() {
                return user_id;
            }

            public String getMessage() {
                return message;
            }

            public String getNotification_date() {
                return notification_date;
            }

            public String getGapwith_date() {
                return gapwith_date;
            }

            public String getStatus() {
                return status;
            }

            @SerializedName("user_id")
            @Expose
            private String user_id;

            @SerializedName("message")
            @Expose
            private String message;

            @SerializedName("notification_date")
            @Expose
            private String notification_date;

            @SerializedName("gapwith_date")
            @Expose
            private String gapwith_date;

            @SerializedName("status")
            @Expose
            private String status;

        }
    }
    public class ChangePasswordResponce {

        @SerializedName("status")
        @Expose
        private int status;

        @SerializedName("message")
        @Expose
        private String message;

        public int getStatus() {return status;}

        public String getMessage() {
            return message;
        }

        class Data{
            public String getUserId() {
                return user_id;
            }

            @SerializedName("user_id")
            @Expose
            private String user_id;
        }

    }
    public class NeedHelpResponce {

        @SerializedName("status")
        @Expose
        private int status;

        @SerializedName("message")
        @Expose
        private String message;

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
    public class EditProfileResponce {

        @SerializedName("status")
        @Expose
        private int status;

        @SerializedName("message")
        @Expose
        private String message;

        public int getStatus() {return status;}

        public String getMessage() {
            return message;
        }

        class Data{
            public String getUserId() {
                return user_id;
            }

            @SerializedName("user_id")
            @Expose
            private String user_id;

            @SerializedName("name")
            @Expose
            private String name;

            @SerializedName("email")
            @Expose
            private String email;

            @SerializedName("profile_pic")
            @Expose
            private String profile_pic;
        }
    }
    public class ShowProfileResponce {

        @SerializedName("status")
        @Expose
        private int status;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("data")
        @Expose
        private Data data;


        public int getStatus() {return status;}

        public String getMessage() {
            return message;
        }

        public Data getData() {
            return data;
        }

        public  class Data{
            public String getUseremail() {
                return useremail;
            }

            public String getFirst_name() {
                return first_name;
            }

            public String getNickname() {
                return nickname;
            }

            public String getLast_name() {
                return last_name;
            }

            public String getPhone() {
                return phone;
            }

            public String getSchoolName() {
                return schoolName;
            }

            public String getSchoolYear() {
                return schoolYear;
            }

            public String getSchool() {
                return school;
            }

            public String getDonor_id() {
                return donor_id;
            }

            public String getImage() {
                return image;
            }

            public String getSms() {
                return sms;
            }

            public String getDob() {
                return dob;
            }

            public String getStatus() {
                return status;
            }

            @SerializedName("useremail")
            @Expose
            private String useremail;

            @SerializedName("first_name")
            @Expose
            private String first_name;

            @SerializedName("nickname")
            @Expose
            private String nickname;

            @SerializedName("last_name")
            @Expose
            private String last_name;

            @SerializedName("phone")
            @Expose
            private String phone;

            @SerializedName("schoolName")
            @Expose
            private String schoolName;

            @SerializedName("schoolYear")
            @Expose
            private String schoolYear;

            @SerializedName("school")
            @Expose
            private String school;

            @SerializedName("donor_id")
            @Expose
            private String donor_id;

            @SerializedName("image")
            @Expose
            private String image;

            @SerializedName("sms")
            @Expose
            private String sms;

            @SerializedName("dob")
            @Expose
            private String dob;

            @SerializedName("status")
            @Expose
            private String status;
        }
    }
    public class ForgetPasswordResponce {

        @SerializedName("status")
        @Expose
        private int status;

        @SerializedName("message")
        @Expose
        private String message;

        public int getStatus() {return status;}

        public String getMessage() {
            return message;
        }

    }

    public class UserCountsResponce {

        @SerializedName("status")
        @Expose
        private int status;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("data")
        @Expose
        private Data data;


        public int getStatus() {return status;}

        public String getMessage() {
            return message;
        }

        public Data getData() {
            return data;
        }

        public  class Data{

            public String getDonation() {
                return donation;
            }

            public String getDonation_count() {
                return donation_count;
            }

            public String getVoluteer() {
                return voluteer;
            }

            public String getVoluteer_count() {
                return voluteer_count;
            }

            public String getRecruit() {
                return recruit;
            }

            public String getRecruit_count() {
                return recruit_count;
            }

            public String getHost() {
                return host;
            }

            public String getHost_count() {
                return host_count;
            }

            public String getUserid() {
                return userid;
            }

            public String getUserinfo() {
                return userinfo;
            }

            public String getUserpostdate() {
                return userpostdate;
            }

            public String getRecuruiter_name() {
                return recuruiter_name;
            }

            public String getPoststatus() {
                return poststatus;
            }

            public String getNotificationcount() {
                return notificationcount;
            }

            public int getCountAward() {
                return countAward;
            }

            @SerializedName("donation")
            @Expose
            private String donation;

            @SerializedName("donation_count")
            @Expose
            private String donation_count;

            @SerializedName("voluteer")
            @Expose
            private String voluteer;

            @SerializedName("voluteer_count")
            @Expose
            private String voluteer_count;

            @SerializedName("recruit")
            @Expose
            private String recruit;

            @SerializedName("recruit_count")
            @Expose
            private String recruit_count;

            @SerializedName("host")
            @Expose
            private String host;

            @SerializedName("host_count")
            @Expose
            private String host_count;

            @SerializedName("userid")
            @Expose
            private String userid;

            @SerializedName("userinfo")
            @Expose
            private String userinfo;

            @SerializedName("userpostdate")
            @Expose
            private String userpostdate;

            @SerializedName("recuruiter_name")
            @Expose
            private String recuruiter_name;

            @SerializedName("poststatus")
            @Expose
            private String poststatus;

            @SerializedName("notificationcount")
            @Expose
            private String notificationcount;

            @SerializedName("countAward")
            @Expose
            private int countAward;

            public String getShare_url() {
                return share_url;
            }

            @SerializedName("share_url")
            @Expose
            private String share_url;
        }
    }

}
