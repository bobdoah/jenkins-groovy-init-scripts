import jenkins.model.*
import hudson.security.*
import hudson.plugins.active_directory.*
   
String domain = System.getenv('ACTIVE_DIRECTORY_DOMAIN')
String bindUser = System.getenv('ACTIVE_DIRECTORY_USER')
String passwordFile = System.getenv('ACTIVE_DIRECTORY_PASSWORD_FILE')

if (domain == null || bindUser == null || passwordFile == null){
    println("--> Skipping AD configuration.")
    return
}

InetAddress address
try {
    address = InetAddress.getByName(domain);
} catch (UnknownHostException e) {
    println("unable to resolve domain: ${domain} with: ${e.getMessage()}")
    println("--> Skipping AD configuration.")
    return
}
if(address == null){
    println("unable to resolve domain: ${domain}")
    println("--> Skipping AD configuration.")
}

String server = address.getHostAddress()
String bindPassword = new File(passwordFile).getText()
String site = System.getenv("ACTIVE_DIRECTORY_SITE") ?: ""

def instance = Jenkins.getInstance()
adrealm = new ActiveDirectorySecurityRealm(domain, site, bindUser, bindPassword, server)
instance.setSecurityRealm(adrealm)
