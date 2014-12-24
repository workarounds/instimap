package com.mrane.sync;


import android.util.Log;

public class NetworkRequest {
    public static final int GET = ServiceHandler.GET;
    public static final int POST = ServiceHandler.POST;
    private String baseUrl = "http://workarounds.in/vnb/api/";
    private String url;
    private int type;
    public String data = null;

    public NetworkRequest(String controller, String functionName, String param, int type) {
        this.url = this.baseUrl + controller + '/' + functionName + '/' + param;
        this.type = type;

        this.data = this.getExtractedResponse();
    }

    public NetworkRequest(String controller, String functionName, String param) {
        this.url = this.baseUrl + controller + '/' + functionName + '/' + param;
        this.type = GET;

        this.data = this.getExtractedResponse();
    }

    public NetworkRequest(String controller, String functionName, int type) {
        this.url = this.baseUrl + controller + '/' + functionName;
        this.type = type;

        this.data = this.getExtractedResponse();
    }

    public NetworkRequest(String controller, String functionName) {
        this.url = this.baseUrl + controller + '/' + functionName;
        this.type = GET;

        this.data = this.getExtractedResponse();
    }

    private String getExtractedResponse() {
        String result = null;
        ServiceHandler serviceHandler = new ServiceHandler();
        String response = serviceHandler.makeServiceCall(this.url, this.type);
        if(response != null) {
            Log.d("NetworkRequest", "" + response);
        }

        return result;
    }
}
