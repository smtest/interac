package com.ibm.interac.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.interac.db.mongo.PayeeRepository;
import com.ibm.interac.db.mongo.TransferRepository;
import com.ibm.interac.db.mongo.UserRepository;
import com.ibm.interac.domain.Payee;
import com.ibm.interac.domain.Transfer;
import com.ibm.interac.domain.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class InteracServiceImpl implements InteracService
{
	private static final Logger logger = LoggerFactory.getLogger(InteracServiceImpl.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	@Autowired
	PayeeRepository payeeRepo;

	@Autowired
	TransferRepository transferRepo;

	@Autowired
	UserRepository userRepo;

	@Override
	@HystrixCommand(fallbackMethod = "getDummyTransfer", commandProperties = {
			@HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE") })
	public Transfer find(String id)
	{
		logger.debug("Finding Transfer for id: %s", id);
		return transferRepo.findOne(id);
	}

	public Transfer getDummyTransfer(String id)
	{
		return null;
	}

	@Override
	public Transfer createTransfer(Transfer transfer)
	{
		Validate.notNull(transfer, "input transfer can't be null");
		String id = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);

		transfer.setId(id);

		transfer.setCreatedAt(DATE_FORMAT.format(new Date()));

		transfer.setState("Pending");

		return transferRepo.save(transfer);
	}

	@Override
	public Payee createPayee(Payee payee)
	{
		Validate.notNull(payee, "input payee can't be null");
		return payeeRepo.save(payee);
	}

	@Override
	public void saveChanges(Transfer update)
	{
		Validate.notNull(update, "input update can't be null");
		Validate.notEmpty(update.getId(), "input update.id can't be null or empty");
		Validate.notEmpty(update.getState(), "input update.state can't be null or empty");
		Validate.notNull(update.getAmount(), "input update.amount can't be null or empty");

		Transfer orig = transferRepo.findOne(update.getId());

		Validate.notNull(orig, "no Transfer found for id: %s", update.getId());

		orig.setState(update.getState());
		orig.setAmount(update.getAmount());

		transferRepo.save(orig);
	}

	@Override
	public User createUser(User user)
	{
		Validate.notNull(user, "input user can't be empty or null");
		Validate.notEmpty(user.getUsername(), "input user.username can't be null or empty");

		user.setPassword(null);

		return userRepo.save(user);

	}

}
