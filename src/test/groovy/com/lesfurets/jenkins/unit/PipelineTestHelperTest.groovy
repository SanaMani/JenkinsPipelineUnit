package test.trta.dtxp.shared.artifactory

import trta.dtxp.shared.artifactory.artifactoryHelper
import trta.dtxp.shared.artifactory.artifactoryConfigurartion
import trta.dtxp.shared.notifications.iTeamsConfigurationProvider
import trta.dtxp.shared.artifactory.bamsArtifactoryServer
import trta.dtxp.shared.artifactory.iArtifactoryServer
import trta.dtxp.shared.iConfigReader

import org.junit.Test
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import static org.mockito.Mockito.*
import static org.junit.Assert.*

//
// Class contains test methods for artifactoryHelper
//
class artifactoryHelperTest extends GroovyTestCase 
{ 
    //Setup test environment
    @Before
    void setUp() {
         
    }  
    
    //Cleanup test
    @After
    void tearDown(){
         
    } 

    @Test
    void test_artifactoryHelper_constructort() {

        //Arrange
        def artifactoryHelperObject = getartifactoryHelperObject() 
        
        //Assert 
        aassertNotEquals(null,artifactoryHelperObject)
    }
    
    @Test
    void test_toArtifactoryConfigurartion() {

        //Arrange
        def artifactoryHelperObject = getartifactoryHelperObject()
        def artifactoryConfigurartionTestObj = new artifactoryConfigurartion("0.0.0.0", "bamsUploadTarget", "branchName", "fileName")
        //Act        
        def result =  artifactoryHelperObject.toArtifactoryConfigurartion(artifactoryConfigurartionTestObj)
        //Assert
        assertNotEquals(null,result)
        assertEquals("0.0.0.0",result.version)
        assertEquals("bamsUploadTarget",result.bamsUploadTarget)
        assertEquals("branchName",result.branchName)
        assertEquals("fileName",result.fileName)
    }

    @Test
    void test_validateArtifactoryConfigurartion_artifactoryConfigurartion_null_exception(){

        //Arrange
        def artifactoryHelperObject = getartifactoryHelperObject()       
        try
        {
            //Act 
            def result =  artifactoryHelperObject.validateArtifactoryConfigurartion(null)
        }
        catch(e)
        {
            //Assert
            assert e.message.contains("artifactoryConfigurartion is null")
        }        
    }
    
    @Test
    void test_validateArtifactoryConfigurartion_artifactoryConfigurartion_version_empty_exception(){

        //Arrange
        def artifactoryHelperObject = getartifactoryHelperObject()
        def artifactoryConfigurartionObj = new artifactoryConfigurartion("", "bamsUploadTarget", "branchName", "fileName")        
        try
        {   
            //Act           
            def result =  artifactoryHelperObject.validateArtifactoryConfigurartion(artifactoryConfigurartionObj)
        }
        catch(e)
        {
            //Assert
            assert e.message.contains("version not found")
        }            
    }

    @Test 
    void test_validateArtifactoryConfigurartion_artifactoryConfigurartion_bamsUploadTarget_empty_exception(){

        //Arrange
        def artifactoryHelperObject = getartifactoryHelperObject()
        def artifactoryConfigurartionObj = new artifactoryConfigurartion("0.0.0.0", "", "branchName", "fileName")        
         try
         { 
            //Act            
            def result =  artifactoryHelperObject.validateArtifactoryConfigurartion(artifactoryConfigurartionObj)
         }
        catch(e)
        {
            //Assert
            assert e.message.contains("bamsUploadTarget not found")
        }        
    }

    @Test                                
    void test_validateArtifactoryConfigurartion_artifactoryConfigurartion_fileName_empty_exception(){

        //Arrange
        def artifactoryHelperObject = getartifactoryHelperObject()
        def artifactoryConfigurartionObj = new artifactoryConfigurartion("0.0.0.0", "bamsUploadTarget", "branchName", "")
        try
        { 
            //Act             
            def result =  artifactoryHelperObject.validateArtifactoryConfigurartion(artifactoryConfigurartionObj)
        }
        catch(e)
        {
            //Assert
            assert e.message.contains("fileName not found")
        }        
    }

    @Test                                 
    void test_validateArtifactoryConfigurartion_valid_branchName_empty_exception(){

        //Arrange   
        def artifactoryHelperObject = getartifactoryHelperObject()
        def artifactoryConfigurartionObj = new artifactoryConfigurartion("0.0.0.0", "bamsUploadTarget", "", "fileName")                
        try
         {      
            //Act        
            def result =  artifactoryHelperObject.validateArtifactoryConfigurartion(artifactoryConfigurartionObj)
         }
        catch(e)
        {
            //Assert
            assert e.message.contains("branchName not found")
        }        
    }                               
                                      
    @Test
    void test_validateArtifactoryConfigurartion_valid_param_noException(){

        //Arrange
        def artifactoryHelperObject = getartifactoryHelperObject()
        def artifactoryConfigurartionObj = new artifactoryConfigurartion("0.0.0.0", "bamsUploadTarget", "branchName", "fileName")              
        //Act
        def result = artifactoryHelperObject.validateArtifactoryConfigurartion(artifactoryConfigurartionObj)        
        //Assert
        assertEquals(null,result)
    }
    
    @Test
    void test_pushToArtifactory()
    {
        //Arrange
        def bamsArtifactoryServerTestObjj =  mock(iArtifactoryServer.class);
        when(bamsArtifactoryServerTestObjj.pushArtifacts(any(artifactoryConfigurartion.class))).thenReturn(null);
        def artifactoryHelperObject = new artifactoryHelper(bamsArtifactoryServerTestObjj)
        def artifactoryConfigurartionObj = new artifactoryConfigurartion("0.0.0.0", "bamsUploadTarget", "branchName", "fileName")        
        //Act
        def result = artifactoryHelperObject.pushToArtifactory(artifactoryConfigurartionObj)        
        //Assert
        assertEquals(null,result)
    }
    
    @Test
    void test_getArtifactoryConfiguration_invalid_projectName_exception()
    {
        //Arrange
        def artifactoryHelperObject = getartifactoryHelperObject() 
        def configReader = mock(iConfigReader.class); 
        when(configReader.getConfigData(any(artifactoryConfigurartion.class))).thenReturn(new groovy.json.JsonSlurper().parseText('{"testProject": [{"name": "testBranch","version": "0.0.0.0","bamsUploadTarget": "testPath","branchName": "testBranch","fileName": "*.zip"} ]}') );         
        try
        {   
            //Act           
            def configData = artifactoryHelperObject.getArtifactoryConfiguration("testBranch", "test",configReader)
        }
        catch(e)
        {
            //Assert
            assert e.message.contains("No data found in config file")
        }           
    }    
    
    @Test
    void test_getArtifactoryConfiguration_no_data_in_configFile ()
    {
        //Arrange
        def artifactoryHelperObject = getartifactoryHelperObject() 
        def configReader = mock(iConfigReader.class); 
        when(configReader.getConfigData(any(artifactoryConfigurartion.class))).thenReturn(new groovy.json.JsonSlurper().parseText('{"testProject": [{"name": "testBranch","version": "0.0.0.0","bamsUploadTarget": "testPath","branchName": "testBranch","fileName": "*.zip"} ]}') );            
        try
        {     
            //Act         
            def configData = artifactoryHelperObject.getArtifactoryConfiguration("testBranch", "testProject",configReader)
        }
        catch(e)
        {
            //Assert
            assert e.message.contains("No data found in config file")
        }           
    }
    
    @Test
    void test_getArtifactoryConfiguration()
    {
        //Arrange
        def artifactoryHelperObject = getartifactoryHelperObject() 
        def configReader = mock(iConfigReader.class); 
        when(configReader.getConfigData(any(artifactoryConfigurartion.class))).thenReturn(new groovy.json.JsonSlurper().parseText('{"testProject": [{"name": "testBranch","version": "0.0.0.0","bamsUploadTarget": "testPath","branchName": "testBranch","fileName": "*.zip"} ]}') );               
        //Act
        def result = artifactoryHelperObject.getArtifactoryConfiguration("testBranch", "testProject",configReader)
        //Assert
        assertNotEquals(null,result)
        assertEquals("testBranch",result.branchName)
        assertEquals("testFile",result.fileName)
    }

    //Helper methods
    def getartifactoryHelperObject()
    {
        def mockIArtifactoryServer = Mockito.mock(iTeamsConfigurationProvider.interface);
        return new artifactoryHelper(mockIArtifactoryServer)
    }
}
