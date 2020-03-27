import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class will save you nerves when fetching tokenGroups in AD.
 *
 * @author Matthias Kauzmann
 */
@Service
public class TokenGroup {

    private static final String TOKEN_GROUPS = "tokenGroups";
    // get connection settings from properties file dynamically by value injection
    @Value("${active-directory.user-search-base}")
    private String userSearch;
    @Value("${active-directory.url}")
    private String url;
    @Value("${active-directory.manager-dn}")
    private String managerDn;
    @Value("${active-directory.manager-password}")
    private String managerPassword;

    /**
     * Retrieve an object's tokenGroups attribute in AD.
     *
     * @param cN is the canonical name of the object whose tokenGroups shall be found
     * @return a list with string representations of objectSid for each transient membership
     */
    public List<String> getTokenGroups(String cN) throws Exception {
      
        // succeeding lookup() takes a String[] as an argument for the attributes to retrieve
        String[] returns = {TOKEN_GROUPS};
        // instantiate a proper LdapTemplate object
        LdapTemplate ldapTemplate = ldapTemplate();
        // choose the lookup() that takes the distinguished name at first, then attributes and a mapper at last 
        AdObject adObject = ldapTemplate.lookup("CN=" + cN + "," + userSearch, returns,
                new AdRoleGroupAttributesMapper());

        return adObject.getTokenGroups();

    }
    
    /**
     * Build a right LdapTemplate for the purpose of retrieving tokenGroups.
     *
     * @return an LdapTemplate object
     */
    private LdapTemplate ldapTemplate() throws Exception {

        Map<String, Object> envMap = new HashMap<>();
        // declare tokenGroups as a binary attribute in order to prevent compile time error when casting
        envMap.put("java.naming.ldap.attributes.binary", TOKEN_GROUPS);

        LdapContextSource contextSource = new LdapContextSource();
        // set properties + connection
        contextSource.setBaseEnvironmentProperties(envMap);
        contextSource.setUrl(url);
        contextSource.setUserDn(managerDn);
        contextSource.setPassword(managerPassword);
        contextSource.afterPropertiesSet();

        LdapTemplate ldapTemplate = new LdapTemplate(contextSource);
        ldapTemplate.afterPropertiesSet();
        return ldapTemplate;

    }

}
