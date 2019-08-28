# CurriculumTracker
Java Android App built to help students track important dates related to schoolwork. App uses a local SQLite database to 
CRUD assessment, course, and term information. This was an open-ended course project in which we were given a detailed list 
of business requirements and tasked with creating an Android App that satisfied those requirements. 

USE INSTRUCTIONS:
Assessments are added to courses, and courses are added to terms. Add terms first, then you can add courses to a term. Once you've added
a course to a term, you can add assessments to the course.

ALERTS:
You have the option for the app to alert you at the start of a course, or an assessment. If the user confirms setting an alert in the dialog, an AlertManager.RTC_WAKEUP with NotificationCompat.PRIORITY_HIGH will trigger an alarm at 8am, 24 hours before the start of the class/term. 

SHARING:
Gives the user the option to share (text, email) details regarding a course or assessment. 
