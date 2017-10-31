import jenkins.model.*

def adminEmailAddress = System.getenv("ADMIN_EMAIL_ADDRESS") ?: "jenkins@${InetAddress.localHost.canonicalHostName}"

println("Setting admin email address to ${adminEmailAddress}")

def jenkinsLocationConfiguration = JenkinsLocationConfiguration.get()
jenkinsLocationConfiguration.setAdminAddress(adminEmailAddress)
