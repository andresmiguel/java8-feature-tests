package com.ambh.java8;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;

public class AppTest {

	private List<Integer> intList;
	private List<String> strList;
	private List<Person> persons;

	@Before
	public void init() {
		List<Integer> intListTemp = new ArrayList<Integer>();
		List<String> strListTemp = new ArrayList<String>();
		IntStream.range(0, 10).forEach(i -> {
			intListTemp.add(i);
			strListTemp.add(i + "");
		});
		intList = Collections.unmodifiableList(intListTemp);
		strList = Collections.unmodifiableList(strListTemp);

		fillPersons();
	}

	@Test
	public void collectTests() {

		// Copiar de una lista fuente
		List<Integer> ints = intList.stream()
									.collect(() -> new ArrayList<>(), (c, e) -> c.add(e),
											(c1, c2) -> c1.addAll(c2));
		assertThat(intList, equalTo(ints));
		
		// Concatenar elementos de una lista de strings
		StringBuilder stringBuilder = strList.stream()
											 .collect(() -> new StringBuilder(), (sb, e) -> sb.append(e),
													 (sb1, sb2) -> sb1.append(sb2.toString()));		
		assertThat(stringBuilder.toString(), equalTo("0123456789"));
		
	}

	@Test
	public void reduceTests() {
		
		// Realizar suma de todas las edades de la lista de personas 
		int sumOfAges = persons.stream()
							   .reduce(0, 
									   (sum, p) -> sum + p.getAge(), 
									   Integer::sum);
		assertThat(sumOfAges, equalTo(197));
	}

	@Test
	public void specializedReductionFormTests() {
		
		// Contar cantidad de adultos
		long adultsCount = persons.stream()
								.filter(AppTest::isAdult)
								.count();
		assertThat(adultsCount, equalTo(4L));
		
		//  Realizar suma de todas las edades de los adultos de la lista de personas
		int adultAgesSum = persons.stream()
								.filter(AppTest::isAdult)
								.mapToInt(p -> p.getAge())
								.sum();
		assertThat(adultAgesSum, equalTo(183));
		
	}
	
	private static boolean isAdult(Person p) {
		return p.getAge() > 17;
	}
	
	private void fillPersons() {
		persons = new ArrayList<>();

		persons.add(new Person("Juan", "Perez", 55));
		persons.add(new Person("Jose", "Gonzalez", 36));
		persons.add(new Person("Alberto", "Ramos", 69));
		persons.add(new Person("Juana", "", 23));
		persons.add(new Person("Maria", "Perez", 14));

	}
}
