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

import org.apache.ibatis.annotations.*;
import org.openlmis.equipment.domain.EquipmentInventoryStatus;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentInventoryStatusMapper {

  @Select("SELECT eis.*" +
      " FROM equipment_inventory_statuses eis" +
      " WHERE eis.inventoryId = #{inventoryId}" +
      " ORDER BY eis.effectivedatetime DESC LIMIT 1")
  EquipmentInventoryStatus getCurrentStatus(@Param("inventoryId")Long inventoryId);

  @Insert("INSERT INTO equipment_inventory_statuses" +
      " (inventoryId, statusId, notFunctionalStatusId, effectiveDateTime)" +
      " VALUES (#{inventoryId}, #{statusId}, #{notFunctionalStatusId}, NOW())")
  @Options(useGeneratedKeys = true)
  void insert(EquipmentInventoryStatus status);

  @Update("UPDATE equipment_inventory_statuses" +
      " SET inventoryId = #{inventoryId}, statusId = #{statusId}, notFunctionalStatusId = #{notFunctionalStatusId}," +
      " effectiveDateTime = NOW()" +
      " WHERE id = #{id}")
  void update(EquipmentInventoryStatus status);
}
