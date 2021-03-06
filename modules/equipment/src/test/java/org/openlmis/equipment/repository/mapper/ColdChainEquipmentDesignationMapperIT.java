/*
 * This program was produced for the U.S. Agency for International Development. It was prepared by the USAID | DELIVER PROJECT, Task Order 4. It is part of a project which utilizes code originally licensed under the terms of the Mozilla Public License (MPL) v2 and therefore is licensed under MPL v2 or later.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the Mozilla Public License as published by the Mozilla Foundation, either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the Mozilla Public License for more details.
 *
 * You should have received a copy of the Mozilla Public License along with this program. If not, see http://www.mozilla.org/MPL/
 */

package org.openlmis.equipment.repository.mapper;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openlmis.core.query.QueryExecutor;
import org.openlmis.db.categories.IntegrationTests;
import org.openlmis.equipment.domain.ColdChainEquipmentDesignation;
import org.openlmis.equipment.domain.EquipmentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

@Category(IntegrationTests.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:test-applicationContext-equipment.xml")
@Transactional
@TransactionConfiguration(defaultRollback = true, transactionManager = "openLmisTransactionManager")
public class ColdChainEquipmentDesignationMapperIT {

  @Autowired
  ColdChainEquipmentDesignationMapper mapper;

  @Autowired
  QueryExecutor queryExecutor;


  @Test
  public void shouldGetCCEDesignationById() throws Exception {
    ColdChainEquipmentDesignation designation = new ColdChainEquipmentDesignation();
    designation.setName("Test");
    mapper.insert(designation);

    ColdChainEquipmentDesignation result = mapper.getById(designation.getId());
    assertEquals(result.getName(), designation.getName());
  }

  @Test
  public void shouldGetAllCCEDesignations() throws Exception {
    //ColdChainEquipmentDesignation designation = new ColdChainEquipmentDesignation();
    //designation.setName("Test");
   // mapper.insert(designation);

   // ResultSet resultSet = queryExecutor.execute("SELECT * FROM equipment_cold_chain_equipment_designations" );

    List<ColdChainEquipmentDesignation> designations=mapper.getAll();
    assertEquals(designations.size(), 3);
  }

}