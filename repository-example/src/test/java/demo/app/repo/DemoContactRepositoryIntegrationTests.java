/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package demo.app.repo;

import static example.app.model.Contact.newContact;
import static example.app.model.Person.newPerson;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicLong;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import example.app.config.ApplicationConfiguration;
import example.app.model.Contact;
import example.app.repo.gemfire.ContactRepository;
import example.app.repo.gemfire.CustomerRepository;

/**
 * The DemoContactRepositoryIntegrationTests class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = ApplicationConfiguration.class)
@SuppressWarnings("unused")
public class DemoContactRepositoryIntegrationTests {

	private static final AtomicLong ID_SEQUENCE = new AtomicLong(0L);

	@Autowired
	private ContactRepository contactRepository;

	@Autowired(required = false)
	private CustomerRepository customerRepository;

	@After
	public void tearDown() {
		contactRepository.deleteAll();
	}

	protected Long newId() {
		return ID_SEQUENCE.incrementAndGet();
	}

	protected Contact save(Contact contact) {
		if (contact.isNew()) {
			contact.setId(newId());
		}

		return contactRepository.save(contact);
	}

	// TODO create your application configuration
	// TODO Uncomment @DependsOn("Customers") annotation on the Contacts bean in the GemFireConfiguration class

	// 0. CRUD

	@Test
	public void saveFindAndDeleteIsSuccessful() {
		Contact jonDoe = newContact(newPerson("Jon", "Doe"), "jonDoe@work.com").identifiedBy(newId());

		contactRepository.save(jonDoe);

		Contact contact = contactRepository.findOne(jonDoe.getId());

		assertThat(contact).isNotSameAs(jonDoe);
		assertThat(contact).isEqualTo(jonDoe);

		contactRepository.delete(contact);

		assertThat(contactRepository.count()).isEqualTo(0);
	}

	// 1. find by email

	// 2. find by phone number

	// 3. find by address

	// 4. find by person's last name

	// 5. find by person's first and last name ignoring case

	// 6. find by person's last name like; sort by person's last name ascending

	// 7. find by person's age >= 21; sort by person last name ASC and person age DESC

	// 7.1 Demo @Limit(1)
	// 7.2 Demo @Trace & @Hint on findByEmail with "EmailIdx" Geode Index

	// 8. (Optional) find by person's gender

	// TODO reinstate ApplicationConfiguration
	// TODO comment out @DependsOn("Customers") annotation on the Contacts Region bean in the GemFireConfiguration class
	// TODO change Repository class references in this test

	// 9. find by customers with contact information

}
