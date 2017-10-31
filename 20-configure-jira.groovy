import jenkins.model.*                                                                                                 
import hudson.plugins.jira.*                                                                                           
                                                                                                                       
inst = Jenkins.getInstance()                                                                                           
def desc = inst.getDescriptor("hudson.plugins.jira.JiraProjectProperty")                                               

def jiraUrl = System.getenv('JIRA_URL')                                                                                
if (jiraUrl == null){                                                                                                  
    println("No JIRA_URL env var provided.")                                                                           
    println("--> Skipping Jira plugin configuration")                                                                  
    return                                                                                                             
}
jiraUrl = new URL(jiraUrl)

for (JiraSite site: desc.sites){
    if (jiraUrl.toURI().equals(site.url.toURI())){
        println("Jira site ${jiraUrl} already configured")
        println("--> Skipping Jira plugin configuration")
        return
    }
}
println("Configuring Jira instance: ${jiraUrl}")                                                                       
                                                                                                                       
def jiraAlternativeUrl = System.getenv('JIRA_ALT_URL')                                                                 
jiraAlternativeUrl = jiraAlternativeUrl != null ? new URL(jiraAlternativeUrl) : jiraUrl                                                                                                                                                                                                            
                                                                                                                       
def jiraUsername = System.getenv('JIRA_USERNAME')
if (jiraUsername == null){
  jiraUsername = ""
}
                                                                                                                       
def jiraPassword = System.getenv('JIRA_PASSWORD')                                                                      
                                                                                                                       
def jiraSupportsWikiStyleComment = System.getenv('JIRA_SUPPORTS_WIKI_STYLE_COMMENT')                                   
jiraSupportsWikiStyleComment = jiraSupportsWikiStyleComment != null ? jiraSupportsWikiStyleComment.toBoolean() : false 
                                                                                                                       
def jiraRecordScmChanges = System.getenv('JIRA_RECORD_SCM_CHANGES')                                                    
jiraRecordScmChanges = jiraRecordScmChanges != null ? jiraRecordScmChanges.toBoolean() : false                         
                                                                                                                       
def jiraUserPattern = System.getenv('JIRA_USER_PATTERN')                                                               
                                                                                                                       
def jiraUpdateIssueForAllStatus = System.getenv('JIRA_UPDATE_ISSUE_FOR_ALL_STATUS')                                    
jiraUpdateIssueForAllStatus = jiraUpdateIssueForAllStatus != null ? jiraUpdateIssueForAllStatus.toBoolean() : false    
                                                                                                                       
def jiraGroupVisibility = System.getenv('JIRA_GROUP_VISIBILITY')                                                       
def jiraRoleVisibility = System.getenv('JIRA_ROLE_VISIBILITY')                                                         
                                                                                                                       
def jiraUseHTTPAuth = System.getenv('JIRA_USE_HTTP_AUTH')                                                              
jiraUseHTTPAuth = jiraUseHTTPAuth != null ? jiraUseHTTPAuth.toBoolean() : false                                        
                                                                                                                       
def jiraSite = new JiraSite(                                                                                           
    jiraUrl,
    jiraAlternativeUrl,
    jiraUsername,                                                                                                      
    jiraPassword,                                                                                                      
    jiraSupportsWikiStyleComment,                                                                                      
    jiraRecordScmChanges,                                                                                              
    jiraUserPattern,                                                                                                   
    jiraUpdateIssueForAllStatus,                                                                                       
    jiraGroupVisibility,                                                                                               
    jiraRoleVisibility,                                                                                                
    jiraUseHTTPAuth                                                                                                    
)                                                                                                                      

desc.setSites(jiraSite)                                                                                                
desc.save()   
