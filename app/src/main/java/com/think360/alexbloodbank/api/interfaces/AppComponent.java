package com.think360.alexbloodbank.api.interfaces;


import com.think360.alexbloodbank.BloodCenterActivity;
import com.think360.alexbloodbank.LoginRegTabActivity;
import com.think360.alexbloodbank.api.ApplicationModule;
import com.think360.alexbloodbank.api.HttpModule;
import com.think360.alexbloodbank.fragments.AddNewDonationFragment;
import com.think360.alexbloodbank.fragments.ChangePasswordFragment;
import com.think360.alexbloodbank.fragments.DonationFragment;
import com.think360.alexbloodbank.fragments.EditProfileFragment;
import com.think360.alexbloodbank.fragments.EnterBloodDriveInfoFragment;
import com.think360.alexbloodbank.fragments.ForgetFragment;
import com.think360.alexbloodbank.fragments.HomeFragment;
import com.think360.alexbloodbank.fragments.HostFragment;
import com.think360.alexbloodbank.fragments.LoginFragment;
import com.think360.alexbloodbank.fragments.MainFragment;
import com.think360.alexbloodbank.fragments.NeedHelpFragment;
import com.think360.alexbloodbank.fragments.NotificationFragment;
import com.think360.alexbloodbank.fragments.RecordRecruitingEffertsFragment;
import com.think360.alexbloodbank.fragments.RecordVolunteersHoursFragment;
import com.think360.alexbloodbank.fragments.RecruitFragment;
import com.think360.alexbloodbank.fragments.RegisterFragment;
import com.think360.alexbloodbank.fragments.VolunteersFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {HttpModule.class, ApplicationModule.class})
public interface AppComponent {
     void inject(LoginRegTabActivity activity);
     void inject(BloodCenterActivity activity);
     void inject(LoginFragment fragment);
     void inject(RegisterFragment fragment);
     void inject(AddNewDonationFragment fragment);
     void inject(RecordVolunteersHoursFragment fragment);
     void inject(RecordRecruitingEffertsFragment fragment);
     void inject(EnterBloodDriveInfoFragment fragment);
     void inject(HostFragment fragment);
     void inject(DonationFragment fragment);
     void inject(VolunteersFragment fragment);
     void inject(RecruitFragment fragment);
     void inject(NotificationFragment fragment);
     void inject(ChangePasswordFragment fragment);
     void inject(NeedHelpFragment fragment);
     void inject(EditProfileFragment fragment);
     void inject(ForgetFragment fragment);
     void inject(HomeFragment fragment);
     void inject(MainFragment fragment);
}
