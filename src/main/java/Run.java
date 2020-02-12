import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.wso2.carbon.authenticator.stub.LoginAuthenticationExceptionException;
import org.wso2.carbon.identity.application.common.model.xsd.ServiceProvider;
import org.wso2.carbon.identity.application.mgt.stub.IdentityApplicationManagementServiceIdentityApplicationManagementException;
import org.wso2.carbon.identity.application.mgt.stub.IdentityApplicationManagementServiceStub;

import java.rmi.RemoteException;

public class Run {
    public static void main(String[] args) {

        System.setProperty("javax.net.ssl.trustStore", ClientConstants.TRUSTSTORE_PATH);
        System.setProperty("javax.net.ssl.trustStorePassword", ClientConstants.TRUSTSTORE_PASSWORD);
        System.setProperty("javax.net.ssl.trustStoreType", ClientConstants.TRUSTSTORE_TYPE);
        
        try {
            AdminServiceClient adminServiceClient = new AdminServiceClient(ClientConstants.TENANT_DOMAIN2);
            String sessionCookie = adminServiceClient.authenticate(ClientConstants.TENANT_ADMIN_2,
                    ClientConstants.ADMIN_PASSWORD);
            IdentityApplicationManagementServiceStub identityApplicationManagementServiceStub =
                    adminServiceClient.getIdentityApplicationManagementServiceStub();

            ServiceClient serviceClient = identityApplicationManagementServiceStub._getServiceClient();
            Options userAdminOption = serviceClient.getOptions();
            userAdminOption.setManageSession(true);
            userAdminOption.setProperty(HTTPConstants.COOKIE_STRING, sessionCookie);
            ServiceProvider serviceProvider =
                    identityApplicationManagementServiceStub.getApplication(ClientConstants.APP_NAME2);
            System.out.println(serviceProvider.getApplicationName());
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (LoginAuthenticationExceptionException e) {
            e.printStackTrace();
        } catch (IdentityApplicationManagementServiceIdentityApplicationManagementException e) {
            e.printStackTrace();
        }
    }

}
