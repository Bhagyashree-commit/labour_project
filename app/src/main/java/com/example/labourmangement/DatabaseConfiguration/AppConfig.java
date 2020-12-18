package com.example.labourmangement.DatabaseConfiguration;

public class AppConfig {

 public static String BASE_URL ="http://example.i-tech.consulting/labour_management/";

    public static String URL_LOGINADMIN="http://example.i-tech.consulting/labour_management/login_admin.php";

    public static String URL_REGISTERLABOUR=BASE_URL+"register_labour.php";

    public static String URL_LOGINCONTRACTOR=BASE_URL+"login_contractor.php";

    public static String URL_REGISTERCONTRACTOR=BASE_URL+"register_contractor.php";

    public static String URL_INSERTLABORDATA =BASE_URL+ "insert_labordata.php ";

    public static String URL_GETJOBOFFER =BASE_URL+ "get_job_offer.php";

    public static String URL_INSERTCONTRACTORDATA =BASE_URL+ "insert_contractordata.php";

    public static String URL_INSERTJOB = BASE_URL+"insert_job.php";

    public static String URL_INSERTAPPLIEDJOB =BASE_URL+ "insert_appliedjob.php";

    public  static String URL_REGISTER_DEVICE=BASE_URL+"RegisterDevice.php";

    public static final String URL_SEND_SINGLE_PUSH = BASE_URL+"sendSinglePush.php";

    public static final String URL_SEND_MULTIPLE_PUSH =BASE_URL+ "sendMultiplePush.php";

    public static final String URL_FETCH_DEVICES =BASE_URL+ "GetRegisteredDevices.php";

    public  static  final  String URL_GETAPPLIEDJOBS=BASE_URL+"get_appliedjob.php";

     public static  final String URL_INSERTAPPROVALREQUEST=BASE_URL+"insertapprovalrequest.php";

     public static final String URL_INSERTREJECTION=BASE_URL+"insert_rejectrequest.php";

     public static final String URL_GETJOBSTATUS=BASE_URL+"get_approvedjobstatsus.php";

    public static final String URL_GETJOBSTATUSBYID=BASE_URL+"get_approvedjobstatsus_byid.php";

    public static final String UPDATE_LOCATION=BASE_URL+"location.php";

   public static final String UPDATE_LAST_LOCATION=BASE_URL+"last_location.php";

   public static final String URL_GETTRACKINGLABOR=BASE_URL+"getlabortracking.php";

   public static final String URL_GETALLDATAOFLABOR=BASE_URL+"getallLaborData.php";

    public static final String URL_GETWAGES=BASE_URL+"getwages.php";

    public static final String URL_GETWAGESTOLABOR=BASE_URL+"getwagestolabor.php";

    public static final String URL_WAGESREQUESTINSERT=BASE_URL+"insertwagerequest.php";

    public static final String URL_GETMONEYREQUEST=BASE_URL+"getwagesrequest.php";

    public static final String URL_MONEYAPPROVEDREQUEST=BASE_URL+"insert_approverequest_wages.php";

    public  static  final  String URL_PAYMENTSTATUS=BASE_URL+"getpaymentstatus.php";

    public static final  String URL_INSERTENGRESPONSE=BASE_URL+"insert_engineer_response.php";

    public static final  String URL_GETPAYMENTREQUEST=BASE_URL+"getpayment_request.php";

    public static  final  String URL_ALLJOBS=BASE_URL+ "alljobs.php";

    public static  final  String URL_RESETPASSWORD=BASE_URL+ "resetPass.php";

    public static final  String URL_GETCONTRACTORDATA=BASE_URL+"getcontractordetails.php";

    public static final  String URL_CHANGETRACKSTATUS=BASE_URL+"approvalstatus.php";

    public static final  String URL_FORGET=BASE_URL+"forgetPassAPI.php";

    public static final  String URL_FORGETPASS="https://2factor.in/API/V1/b55a6f55-2fc6-11eb-83d4-0200cd936042/SMS/";

    public static final  String URL="https://2factor.in/API/V1/b55a6f55-2fc6-11eb-83d4-0200cd936042/SMS/VERIFY/";
}

