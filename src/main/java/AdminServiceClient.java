import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.wso2.carbon.authenticator.stub.AuthenticationAdminStub;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.authenticator.stub.LogoutAuthenticationExceptionException;
import org.wso2.carbon.identity.application.mgt.stub.IdentityApplicationManagementServiceStub;

import java.rmi.RemoteException;

public class AdminServiceClient {

    private AuthenticationAdminStub authenticationAdminStub;

    public IdentityApplicationManagementServiceStub getIdentityApplicationManagementServiceStub() {

        return identityApplicationManagementServiceStub;
    }

    private IdentityApplicationManagementServiceStub identityApplicationManagementServiceStub;
    public AdminServiceClient(String tenatDoamin) throws AxisFault {

        setLoginAdminService(tenatDoamin);
        setIdentityApplicationManagementServiceStub(tenatDoamin);
    }

    private void setLoginAdminService(String tenantDoamin) throws AxisFault {

        String authenticationAdminServiceURL =
                ClientConstants.KM_BASE_URL + "/t/" + tenantDoamin + ClientConstants.AUTHENTICATION_ADMIN;
        authenticationAdminStub = new AuthenticationAdminStub(authenticationAdminServiceURL);
//                ClientConstants.KM_BASE_URL + ClientConstants.AUTHENTICATION_ADMIN;
//        authenticationAdminStub = new AuthenticationAdminStub(authenticationAdminServiceURL);
    }

    private void setIdentityApplicationManagementServiceStub(
            String tenatDomain) throws AxisFault {

        String appMgtServiceURL = ClientConstants.KM_BASE_URL + "/t/" + tenatDomain +
                ClientConstants.IDENTITY_APPLICATION_MANAGEMENT_SERVICE;

        identityApplicationManagementServiceStub = new IdentityApplicationManagementServiceStub(appMgtServiceURL);
    }

    public String authenticate(String userName, String password) throws RemoteException,
            LoginAuthenticationExceptionException {

        String sessionCookie = null;

        if (authenticationAdminStub.login(userName, password, "localhost")) {
            System.out.println("Login Successful");


        }

        ServiceContext serviceContext = authenticationAdminStub.
                _getServiceClient().getLastOperationContext().getServiceContext();
        System.out.println(sessionCookie);
        sessionCookie = (String) serviceContext.getProperty(HTTPConstants.COOKIE_STRING);

        return sessionCookie;
    }

    public void logOut() throws RemoteException, LogoutAuthenticationExceptionException {

        authenticationAdminStub.logout();
    }

}
