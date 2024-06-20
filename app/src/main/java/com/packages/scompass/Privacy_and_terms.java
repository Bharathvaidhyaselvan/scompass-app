package com.packages.scompass;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Privacy_and_terms extends AppCompatActivity {
    TextView text,title;
    String title1 = "Terms & Services",title2 = "Privacy Policy";
    String text1 = "Acceptance of Terms\n" +
            "\n" +
            "Welcome to Scompass, a community-driven travel app! By accessing or using Scompass, you agree to be bound by the following terms and conditions. If you do not agree with any part of these terms, please refrain from using our services.\n" +
            "User Eligibility\n" +
            "\n" +
            "To use Scompass, you must be at least 18 years old. By accessing our services, you confirm that you meet this age requirement.\n" +
            "\n" +
            "Account Registration\n" +
            "\n" +
            "To unlock the full potential of Scompass, users are required to register for an account. During registration, you agree to provide accurate, current, and complete information. It is your responsibility to update this information to ensure its accuracy.\n" +
            "\n" +
            "Privacy Policy\n" +
            "\n" +
            "Your use of Scompass is also governed by our Privacy Policy, available at [place the link of privacy policy]. Please review this policy to understand how we collect, use, and protect your personal information.\n" +
            "\n" +
            "User-Generated Content\n" +
            "\n" +
            "Ownership and License: Any content, including but not limited to images and text, that you upload or post on Scompass remains your property. By posting content, you grant Scompass a worldwide, royalty-free, non-exclusive license to use, modify, adapt, publicly perform, and display such content for the purpose of providing and promoting Scompass services. This license is revocable at any time.\n" +
            "\n" +
            "Content Guidelines: Users are expected to adhere to community guidelines. Prohibited content includes offensive material, hate speech, illegal content, or any content that violates the rights of others.\n" +
            "\n" +
            "User Responsibility: You are solely responsible for the accuracy, legality, and appropriateness of the content you upload. Scompass does not endorse or assume any liability for user-generated content.\n" +
            "\n" +
            " User Conduct\n" +
            "\n" +
            "Community Guidelines: Users must adhere to our community guidelines, promoting respect, inclusivity, and positive interactions. Harassment, bullying, or any inappropriate behavior is strictly prohibited.\n" +
            "\n" +
            "Respect for Others: Respect the opinions and privacy of fellow users. Any form of discrimination, hate speech, or invasion of privacy will not be tolerated.\n" +
            "\n" +
            "Safety: Users should prioritize safety during their travels and adhere to local laws. Scompass is not liable for incidents or accidents during trips organized through the app.\n" +
            "\n" +
            "Trip Planning and Coordination\n" +
            "\n" +
            "User Responsibility: Users are responsible for planning and coordinating trips with other users. Scompass does not assume any liability for the actions or behavior of users during trips.\n" +
            "\n" +
            "Safety Guidelines: Users should follow safety guidelines and local laws during their travels. Scompass encourages users to exercise caution and diligence in trip planning.\n" +
            "\n" +
            " Intellectual Property\n" +
            "\n" +
            "Trademarks: The Scompass name, logo, and associated trademarks are the exclusive property of Scompass. Unauthorized use of these trademarks is strictly prohibited.\n" +
            "\n" +
            "Third-Party Content: Users must respect the intellectual property rights of others. If you believe that your work has been used in a way that constitutes copyright infringement, please contact us promptly.\n" +
            "\n" +
            "Termination of Accounts\n" +
            "\n" +
            "Scompass reserves the right to terminate accounts that violate these terms or for any other reason deemed necessary by Scompass. Users will be notified of any such termination.\n" +
            "\n" +
            "Changes to Terms\n" +
            "\n" +
            "Scompass reserves the right to update or modify these terms at any time without prior notice. Users are encouraged to review the terms regularly. Continued use of Scompass after such changes constitutes acceptance of the updated terms.\n" +
            "\n" +
            "If you have any questions or concerns regarding these terms, please contact us at scompassqueries@gmail.com\n" +
            "\n" +
            "Thank you for being a part of the Scompass community!\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n";
    String text2 = "Welcome to Scompass, a travel community app that values your privacy. This Privacy Policy outlines how we collect, use, disclose, and protect your personal information. By using Scompass, you agree to the terms of this policy.\n" +
            "\n" +
            "Information We Collect\n" +
            "\n" +
            "Account Information: To provide personalized services, we collect information during the account registration process, including your name, email address, profile picture, and other details like passion, city, country, gender.\n" +
            "\n" +
            "User-Generated Content: Content you upload, including location-based posts, photos, and text, and your tours everything is stored on our servers.\n" +
            "\n" +
            "Location Information: Scompass utilizes location-based features to provide real-time travel information. Your device's GPS data and IP address may be collected to determine your location. You can control location sharing through your device settings.\n" +
            "\n" +
            "Usage Data: We collect information about your interactions with Scompass, such as the content your view, features your use, and the frequency and duration of your activities.\n" +
            "\n" +
            "Device Information: We may collect device information, including device type, operating system, and unique device identifiers.\n" +
            "\n" +
            " How We Use Your Information\n" +
            "\n" +
            "Personalization: We use your information to personalize your Scompass experience, including tailoring content and suggestions based on your preferences and location.\n" +
            "\n" +
            "Communication: We may use your email address to send updates, newsletters, or important notifications about Scompass. You can opt out of promotional emails at any time.\n" +
            "\n" +
            "Improvements: Your data helps us enhance and improve Scompass, including developing new features and fixing issues.\n" +
            "\n" +
            "Location-Based Features\n" +
            "\n" +
            "Automatic Location Filtering: Scompass may filter content based on your location to provide you with relevant and local information. You can adjust location-sharing settings within the app.\n" +
            "\n" +
            "Real-Time Posts: When you make a location-based post, your current location may be displayed to other users.\n" +
            "\n" +
            "Information We Collect about tour feature : \n" +
            "\n" +
            "Personal Information: When you create a tour or organize a trip, we collect personal information such as your name, email address, and gender , profile picture, location, chat interactions for safety purposes.\n" +
            "\n" +
            "Trip Details: We collect information about the tour including its itinerary, location, date, and any other details you provide.  Use of Information:\n" +
            "\n" +
            "Trip Organization: We use the information you provide about your tours to facilitate the organization and management of travel activities on the app. \n" +
            "\n" +
            "Communication: We may use your contact information to communicate with you and other participants regarding trip details and updates.\n" +
            "\n" +
            "Sharing of Information: \n" +
            "\n" +
            "With Participants: Your trip details may be shared with other users who join your tour or express interest in participating.\n" +
            "\n" +
            "With Service Providers: We may share necessary information with third-party service providers to assist in facilitating the tour. \n" +
            "User Responsibilities: \n" +
            "\n" +
            "Respect for Privacy: Users are responsible for respecting the privacy and personal boundaries of others during the tour and interactions on the app. \n" +
            "\n" +
            "Accuracy of Information: Users are responsible for providing accurate and up-to-date information about their tours to ensure smooth coordination and communication.\n" +
            "\n" +
            "Data Security\n" +
            "\n" +
            "Encryption: We employ industry-standard encryption protocols to protect your data during transmission.\n" +
            "\n" +
            "Secure Servers: Your data is stored on secure servers with restricted access. We regularly update security measures to safeguard your information.\n" +
            "\n" +
            "Data Sharing and Disclosure\n" +
            "\n" +
            "Third-Party Services: We may share your information with third-party services that help us operate and improve Scompass. These partners are bound by confidentiality agreements.\n" +
            "\n" +
            "Legal Compliance We may disclose your information if required by law, regulation, or legal process.\n" +
            "\n" +
            "User Control and Rights\n" +
            "\n" +
            "Access and Modification: You can access and modify your account information through the app. Some information may be edited or deleted.\n" +
            "\n" +
            "Data Deletion: You can request the deletion of your account and associated data. However, certain information may be retained for legal or operational purposes.\n" +
            "\n" +
            "Changes to the Privacy Policy\n" +
            "\n" +
            "Updates: We may update this Privacy Policy to reflect changes in our practices or legal requirements. You will be notified of significant changes.\n" +
            "\n" +
            "Consent: Continued use of Scompass following changes to the Privacy Policy constitutes your consent to the revised terms.\n" +
            "\n" +
            "Certainly, addressing fraud and clarifying the app's limitations in handling fraudulent activities is an important aspect of the privacy policy. Below is an expanded section that provides more detailed context regarding fraud and the app's responsibility:\n" +
            "\n" +
            " Fraud and Security\n" +
            "\n" +
            "User Responsibility: While Scompass employs robust security measures to protect your information, users must exercise caution and diligence to protect themselves from fraudulent activities. Be wary of sharing sensitive personal information and report any suspicious activities promptly.\n" +
            "\n" +
            "Fraudulent Transactions: Scompass is not responsible for any fraudulent transactions or unauthorized use of your account. Users should take measures to secure their login credentials and report any unauthorized access immediately.\n" +
            "\n" +
            "Phishing and Scams:  Scompass will never request your password or sensitive information through unsolicited emails or messages. Be cautious of phishing attempts and report any suspicious communications claiming to be from Scompass.\n" +
            "\n" +
            "Third-Party Links: Scompass may provide links to third-party websites or services. We are not responsible for the security and privacy practices of these external entities. Users should review the privacy policies of third-party websites before engaging with them.\n" +
            "\n" +
            "Dispute Resolution\n" +
            "\n" +
            "User Disputes Scompass is not responsible for resolving disputes between users. Users are encouraged to communicate directly to resolve any conflicts that may arise during their interactions on the platform.\n" +
            "\n" +
            "Resolution Assistance: While Scompass does not mediate user disputes, we may provide assistance when possible. However, the final responsibility for dispute resolution lies with the involved parties.\n" +
            "\n" +
            "Limitation of Liability\n" +
            "\n" +
            "No Guarantee Against Fraud: Scompass cannot guarantee the absolute prevention of fraudulent activities. Users acknowledge and accept that the internet is not entirely secure, and unforeseen events may occur.\n" +
            "\n" +
            "Indemnification: Users agree to indemnify and hold Scompass and its affiliates harmless from any claims, losses, or damages arising from fraudulent activities or unauthorized access to their accounts.\n" +
            "\n" +
            "Reporting Fraud\n" +
            "\n" +
            "Prompt Reporting: If you believe you have been a victim of fraud or unauthorized access, please report the incident to Scompass immediately through our designated channels.\n" +
            "\n" +
            "Cooperation: Users are encouraged to cooperate with Scompass in investigating and resolving reported incidents. Timely reporting enhances our ability to address and prevent fraudulent activities.\n" +
            "\n" +
            "Fraud Prevention Measures\n" +
            "\n" +
            "Adaptation: Scompass reserves the right to implement new fraud prevention measures. Users will be informed of significant changes that may impact their interactions on the platform.\n" +
            "\n" +
            "Continuous Improvement: Scompass is committed to continuously improving fraud prevention measures and user security. User feedback is valuable in enhancing our security practices.\n" +
            "\n" +
            "By using Scompass, you acknowledge and accept the limitations outlined in this section regarding fraud prevention, dispute resolution, and user responsibility.\n" +
            "\n" +
            "\n" +
            "User Safety and Interactions\n" +
            "\n" +
            "User Responsibility: Scompass is a platform that connects individuals with a shared interest in travel. While the majority of users are genuine and friendly, it is essential to exercise caution when interacting with others.\n" +
            "\n" +
            "Personal Safety: Users are responsible for their personal safety and well-being during interactions with fellow Scompass users. Exercise common sense and take appropriate precautions when meeting someone in person for the first time, especially during travel.\n" +
            "\n" +
            "Vetting and Communication: Before planning trips or meeting other users, consider vetting potential travel companions by thoroughly reviewing their profiles, past interactions, and any shared content. Use the in-app messaging system to communicate and get to know each other better before making any travel arrangements.\n" +
            "\n" +
            "Bad Intentions Warning: Be aware that not all individuals may have good intentions. Some users may approach you with dishonest or harmful motives. It is crucial to remain vigilant and report any suspicious behavior to Scompass immediately.\n" +
            "\n" +
            "Independent Decisions: Users should make independent decisions about whom they choose to interact with, travel with, or share personal information with. Scompass does not perform background checks on users and cannot guarantee the intentions of individuals on the platform.\n" +
            "\n" +
            "Reporting: If you encounter any user who behaves inappropriately, makes you feel uncomfortable, or raises concerns about safety, report the user promptly through the app's reporting mechanisms. Scompass will investigate reports and take appropriate actions, including account suspension or termination.\n" +
            "\n" +
            "Disclaimer of Responsibility: While Scompass endeavors to create a safe and positive community, it cannot guarantee the actions or intentions of individual users. Users acknowledge and agree that they participate in the community and engage in travel activities at their own risk.\n" +
            "\n" +
            "Emergency Situations: In case of emergencies or unsafe situations during a trip organized through Scompass, contact local authorities and emergency services immediately. Scompass is not liable for incidents or accidents during such trips.\n" +
            "\n" +
            "Educational Resources: Scompass may provide educational resources and guidelines on safe travel practices. Users are encouraged to familiarize themselves with these resources and adopt safe travel habits.\n" +
            "\n" +
            "Age Verification: Scompass relies on users to provide accurate information about their age during the registration process. While the platform is designed for users who are 18 years and older, it cannot guarantee the accuracy of user-provided information.\n" +
            "\n" +
            "Travel Insurance: Users are strongly encouraged to obtain appropriate travel insurance to cover unforeseen circumstances or emergencies during their trips.\n" +
            "\n" +
            "By using Scompass, you acknowledge and agree to take responsibility for your own safety and interactions with other users. Scompass disclaims any liability for the actions or behavior of individual users on the platform.\n" +
            "\n" +
            "This section emphasizes user responsibility, encourages caution, and provides guidance on staying safe while using the app. It's crucial to promote a culture of safety within the community.\n" +
            "\n" +
            "If you have questions or concerns about this Privacy Policy, please contact us \n" +
            "\n" +
            "Thank you for trusting Scompass with your travel experiences.\n" +
            "\n" +
            "\n";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_and_terms);

        text = findViewById(R.id.text);
        title = findViewById(R.id.title);

        Intent i = getIntent();
        if (i.getIntExtra("no",0)==1)
        {
            title.setText(title1);
            text.setText(text1);

        }
        else if (i.getIntExtra("no",0)==2)
        {
            title.setText(title2);
            text.setText(text2);

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}