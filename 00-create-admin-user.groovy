import jenkins.model.*
import hudson.security.*

def adminPassword = System.getenv("JENKINS_ADMIN_PASSWORD")

assert adminPassword != null : "No JENKINS_ADMIN_PASSWORD env var provided, but required"

def instance = Jenkins.getInstance()

def hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount('admin', adminPassword)
instance.setSecurityRealm(hudsonRealm)

def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
instance.setAuthorizationStrategy(strategy)
instance.save()
