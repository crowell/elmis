/*
 * This program was produced for the U.S. Agency for International Development. It was prepared by the USAID | DELIVER PROJECT, Task Order 4. It is part of a project which utilizes code originally licensed under the terms of the Mozilla Public License (MPL) v2 and therefore is licensed under MPL v2 or later.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the Mozilla Public License as published by the Mozilla Foundation, either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the Mozilla Public License for more details.
 *
 * You should have received a copy of the Mozilla Public License along with this program. If not, see http://www.mozilla.org/MPL/
 */
package org.openlmis.report.service;

import lombok.NoArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.openlmis.core.domain.Facility;
import org.openlmis.core.service.FacilityService;
import org.openlmis.report.mapper.CCEStorageCapacityReportMapper;
import org.openlmis.report.model.ReportData;
import org.openlmis.report.model.params.CCEStorageCapacityReportParam;
import org.openlmis.report.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static org.openlmis.core.domain.RightName.MANAGE_EQUIPMENT_INVENTORY;

@Component
@NoArgsConstructor
public class CCEStorageCapacityReportDataProvider extends ReportDataProvider {

  @Autowired
  private CCEStorageCapacityReportMapper mapper;

  @Autowired
  private FacilityService facilityService;

  @Override
  protected List<? extends ReportData> getResultSetReportData(Map<String, String[]> params) {
    return getMainReportData(params, null, RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
  }

  @Override
  public List<? extends ReportData> getMainReportData(Map<String, String[]> filterCriteria, Map<String, String[]> sorter, int page, int pageSize) {
    RowBounds rowBounds = new RowBounds((page - 1) * pageSize, pageSize);
    return mapper.getFilteredSortedCCEStorageCapacityReport(getReportFilterData(filterCriteria), rowBounds, this.getUserId());
  }

  public CCEStorageCapacityReportParam getReportFilterData(Map<String, String[]> filterCriteria) {

    CCEStorageCapacityReportParam param = new CCEStorageCapacityReportParam();

    Long programId = StringHelper.isBlank(filterCriteria, "program") ? 0L : Long.parseLong(filterCriteria.get("program")[0]);
    param.setProgramId(programId);
    param.setFacilityLevel(filterCriteria.get("facilityLevel")[0]);

    // List of facilities includes supervised and home facility
    List<Facility> facilities = facilityService.getUserSupervisedFacilities(this.getUserId(), programId, MANAGE_EQUIPMENT_INVENTORY);
    facilities.add(facilityService.getHomeFacility(this.getUserId()));

    StringBuilder str = new StringBuilder();
    str.append("{");
    for (Facility f : facilities) {
      str.append(f.getId());
      str.append(",");
    }
    if (str.length() > 1) {
      str.deleteCharAt(str.length()-1);
    }
    str.append("}");
    param.setFacilityIds(str.toString());

    return param;
  }


}
