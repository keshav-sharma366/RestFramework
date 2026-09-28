package api.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndpoints;
import api.endpoints.UserEndpoints2;
import api.payload.User;
import io.restassured.response.Response;

public class UserTests2 {
	Faker faker;
	User userPayload;
	Logger logger;
	@BeforeClass
	public void setupData()
	{
		faker=new Faker();
		userPayload=new User();

		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5, 10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		
		//logs
		logger= LogManager.getLogger(this.getClass());
		logger.debug("debugging...........");
	}
	
	@Test(priority=1)
	public void testPostUser()

	{
		logger.info("*********** Posting User*************");
		Response response=UserEndpoints2.createUser(userPayload);
		response.then().log().all();

		Assert.assertEquals(response.getStatusCode(),200);
		logger.info("*********** Posted User*************");

	}
	
	@Test(priority=2)
	public void testGetUserByName()

	{
		logger.info("*********** Reading User info *************");
		Response response=UserEndpoints2.readUser(this.userPayload.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(),200);
		logger.info("*********** Read User info *************");
	}
	
	@Test(priority=3)
	public void testUpdateUserByName()

	{
		logger.info("*********** updating User info *************");
		//update data using payload
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());



		Response response=UserEndpoints2.updateUser(this.userPayload.getUsername(),userPayload);
		response.then().log().body();

		Assert.assertEquals(response.getStatusCode(),200);
		
		//Checking data after update
		Response responseAfterupdate=UserEndpoints2.readUser(this.userPayload.getUsername());
		Assert.assertEquals(responseAfterupdate.getStatusCode(),200);
		logger.info("*********** Updated User info *************");
	}
	
	@Test(priority=4)
	public void testDeleteUserByName()

	{
		logger.info("*********** Deleting User info *************");
		Response response=UserEndpoints2.deleteUser(this.userPayload.getUsername());
		Assert.assertEquals(response.getStatusCode(),200);
		logger.info("*********** Deleted User info *************");
	}

}
