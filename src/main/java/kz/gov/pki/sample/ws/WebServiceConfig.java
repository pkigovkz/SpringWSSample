package kz.gov.pki.sample.ws;

import kz.gov.pki.kalkan.jce.provider.KalkanProvider;
import kz.gov.pki.kalkan.jcp.xml.dsig.internal.dom.KalkancryptXMLDSigRI;
import kz.gov.pki.kalkan.xmldsig.KncaXS;
import org.apache.wss4j.common.ConfigurationConstants;
import org.apache.wss4j.common.crypto.Crypto;
import org.apache.wss4j.common.crypto.CryptoFactory;
import org.apache.wss4j.common.crypto.Merlin;
import org.apache.wss4j.common.crypto.WSProviderConfig;
import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.xml.security.utils.Constants;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;
import java.security.Security;
import java.util.List;
import java.util.Properties;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    private String alias = "alias";
    // avoid saving the password in your source codes!
    private char[] password = "pass".toCharArray();
    private String keystoreType = "PKCS12";
    private String keystoreFile = "/path/keystore.p12";

    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        WSProviderConfig.setAddJceProviders(false);
        WSProviderConfig.init(false, false, false);
        Security.addProvider(new KalkanProvider());
        WSProviderConfig.addJceProvider("KalkancryptXMLDSig", new KalkancryptXMLDSigRI());
        KncaXS.loadXMLSecurity();

        // actually, a built-in interceptor is less flexible,
        // though it can be configured for outgoing messages as well as for incoming ones.
        Wss4jSecurityInterceptor interceptor = new Wss4jSecurityInterceptor();
        interceptor.setSecurementSignatureAlgorithm(Constants.MoreAlgorithmsSpecNS+"gost34310-gost34311");
        interceptor.setSecurementSignatureDigestAlgorithm(Constants.MoreAlgorithmsSpecNS + "gost34311");
        interceptor.setSecurementSignatureUser(alias);
        interceptor.setSecurementPassword(new String(password));

        try {
            interceptor.setSecurementSignatureCrypto(obtainSigningCrypto());
        } catch (WSSecurityException e) {
            e.printStackTrace();
        }
        interceptor.setSecurementActions(ConfigurationConstants.SIGNATURE);

        // for incoming messages
//        interceptor.setValidationActions(ConfigurationConstants.SIGNATURE);
//        interceptor.setValidationSignatureCrypto(obtainValidatingCrypto());

        interceptors.add(interceptor);
    }

    private Crypto obtainSigningCrypto() throws WSSecurityException {
        Properties properties = new Properties();
        properties.put(Merlin.PREFIX + Merlin.CRYPTO_KEYSTORE_PROVIDER, KalkanProvider.PROVIDER_NAME);
        properties.put(Merlin.PREFIX + Merlin.CRYPTO_CERT_PROVIDER, KalkanProvider.PROVIDER_NAME);
        properties.put(Merlin.PREFIX + Merlin.KEYSTORE_TYPE, keystoreType);
        properties.put(Merlin.PREFIX + Merlin.KEYSTORE_PASSWORD, new String(password));
        properties.put(Merlin.PREFIX + Merlin.KEYSTORE_ALIAS, alias);
        properties.put(Merlin.PREFIX + Merlin.KEYSTORE_FILE, keystoreFile);

        // don't forget to make your additional validations for certificates,
        // such as extended key usage, revocation status, etc.

        return CryptoFactory.getInstance(properties);
    }

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/ws/*");
    }

    @Bean(name = "shinobies")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema shinobiesSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("ShinobiesPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://pki.gov.kz/sample/soap");
        wsdl11Definition.setSchema(shinobiesSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema shinobiesSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/sample.xsd"));
    }

}
