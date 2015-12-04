/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 * Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openlmis.vaccine.repository.mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface VaccineDashboardMapper {

    @Select("with temp as (\n" +
            "select z.name district, f.name facility_name, f.code facility_code,\n" +
            "       to_char(vr.createdDate, 'DD Mon YYYY') reported_date, \n" +
            "     CASE\n" +
            "            WHEN date_part('day'::text, vr.createddate::date - pp.enddate::date::timestamp without time zone) <= COALESCE((( SELECT configuration_settings.value\n" +
            "               FROM configuration_settings\n" +
            "              WHERE configuration_settings.key::text = 'MSD_ZONE_REPORTING_CUT_OFF_DATE'::text))::integer, 0)::double precision THEN 'T'::text\n" +
            "            WHEN COALESCE(date_part('day'::text, vr.createddate::date - pp.enddate::date::timestamp without time zone), 0::double precision) > COALESCE((( SELECT configuration_settings.value\n" +
            "               FROM configuration_settings\n" +
            "              WHERE configuration_settings.key::text = 'MSD_ZONE_REPORTING_CUT_OFF_DATE'::text))::integer, 0)::double precision THEN 'L'::text\n" +
            "            ELSE 'N'::text\n" +
            "     END AS reporting_status\n" +
            "    from programs_supported ps\n" +
            "    left join vaccine_reports vr on vr.programid = ps.programid and vr.facilityid = ps.facilityid and vr.periodid = 52\n" +
            "    left join processing_periods pp on pp.id = vr.periodid     \n" +
            "    join facilities f on f.id = ps.facilityId \n" +
            "    join geographic_zones z on z.id = f.geographicZoneId\n" +
            "where ps.programId = (select id from programs where enableivdform = 't' limit 1) \n" +
            ") \n" +
            "select\n" +
            "sum(1) expected,\n" +
            "sum(case when reporting_status = 'T' then 1 else 0 end) ontime,\n" +
            "sum(case when reporting_status = 'L' then 1 else 0 end) late,\n" +
            "sum(case when reporting_status = 'N' then 1 else 0 end) not_reported\n" +
            "\n" +
            " from temp t;")
    HashMap<String, Object> getReportingSummary();

    @Select("with temp as (\n" +
            "select z.name district, f.name facility_name, f.code facility_code,\n" +
            "       to_char(vr.createdDate, 'DD Mon YYYY') reported_date, \n" +
            "     CASE\n" +
            "            WHEN date_part('day'::text, vr.createddate::date - pp.enddate::date::timestamp without time zone) <= COALESCE((( SELECT configuration_settings.value\n" +
            "               FROM configuration_settings\n" +
            "              WHERE configuration_settings.key::text = 'MSD_ZONE_REPORTING_CUT_OFF_DATE'::text))::integer, 0)::double precision THEN 'T'::text\n" +
            "            WHEN COALESCE(date_part('day'::text, vr.createddate::date - pp.enddate::date::timestamp without time zone), 0::double precision) > COALESCE((( SELECT configuration_settings.value\n" +
            "               FROM configuration_settings\n" +
            "              WHERE configuration_settings.key::text = 'MSD_ZONE_REPORTING_CUT_OFF_DATE'::text))::integer, 0)::double precision THEN 'L'::text\n" +
            "            ELSE 'N'::text\n" +
            "        END AS reporting_status\n" +
            "    from programs_supported ps\n" +
            "    left join vaccine_reports vr on vr.programid = ps.programid and vr.facilityid = ps.facilityid and vr.periodid = 52\n" +
            "    left join processing_periods pp on pp.id = vr.periodid     \n" +
            "    join facilities f on f.id = ps.facilityId \n" +
            "    join geographic_zones z on z.id = f.geographicZoneId\n" +
            "where ps.programId = (select id from programs where enableivdform = 't' limit 1)\n" +
            " \n" +
            ") select district, facility_name, facility_code, reported_date, reporting_status from temp t;\n")
    HashMap<String, Object> getReportingDetails();


    @Select("select count(1) repairing from (\n" +
            "   select facility_id, facility_name, geographic_zone_name district, equipment_name, model, yearofinstallation year_installed, period_start_date::date date_reported\n" +
            "     from vw_vaccine_cold_chain where upper(status) = 'NOT FUNCTIONAL' and programid = (select id from programs where enableivdform = 't' limit 1)  \n" +
            "      and period_id = 51\n" +
            ") a;\n")
    HashMap<String, Object> getRepairingSummary();


        @Select("  select facility_id, facility_name, geographic_zone_name district, equipment_name, model, yearofinstallation year_installed, period_start_date::date date_reported\n" +
                "     from vw_vaccine_cold_chain where upper(status) = 'NOT FUNCTIONAL' and programid = (select id from programs where enableivdform = 't' limit 1)  \n" +
                "      and period_id = 51;")
        HashMap<String, Object> getRepairingDetails();


        @Select("select count(1) from (\n" +
                " select facility_code, facility_name, geographic_zone_name district, aefi_case, aefi_batch, aefi_date, aefi_notes\n" +
                "   from vw_vaccine_iefi where is_investigated = 'f' \n" +
                "    and program_id = (select id from programs where enableivdform = 't' limit 1) \n" +
                "    and  period_id = 51 \n" +
                ") a;")
        HashMap<String, Object> getInvestigatingSummary();

        @Select(" select facility_code, facility_name, geographic_zone_name district, aefi_case, aefi_batch, aefi_date, aefi_notes\n" +
                "   from vw_vaccine_iefi where is_investigated = 'f' \n" +
                "    and program_id = (select id from programs where enableivdform = 't' limit 1) \n" +
                "    and  period_id = 51; ")
        HashMap<String, Object> getInvestigatingDetails();

        @Select("SELECT\n" +
                "d.region_name,\n" +
                "d.district_name,\n" +
                "i.period_name, \n" +
                "i.period_start_date,\n" +
                "sum(i.denominator) target, \n" +
                "sum(COALESCE(i.within_outside_total,0)) actual,\n" +
                "(case when sum(denominator) > 0 then (sum(COALESCE(i.within_outside_total,0)) / \n" +
                "sum(denominator)::numeric) else 0 end) * 100 coverage\n" +
                "FROM\n" +
                "vw_vaccine_coverage i\n" +
                "JOIN vw_districts d ON i.geographic_zone_id = d.district_id\n" +
                "JOIN vaccine_reports vr ON i.report_id = vr.ID\n" +
                "JOIN program_products pp ON pp.programid = vr.programid\n" +
                "AND pp.productid = i.product_id\n" +
                "WHERE\n" +
                "i.program_id = ( SELECT id FROM programs p WHERE p .enableivdform = TRUE limit 1) \n" +
                "AND i.period_start_date::date>= '2015-01-01' and i.period_end_date::date <= '2015-12-31'\n" +
                "and i.product_id = 2412\n" +
                "group by 1,2,3,4\n" +
                "ORDER BY\n" +
                "d.region_name,\n" +
                "d.district_name,\n" +
                "i.period_start_date")
        List<HashMap<String, Object>> getMonthlyCoverage();

        @Select("SELECT\n" +
                "i.product_name,\n" +
                "i.period_name,\n" +
                "i.period_start_date, \n" +
                "CASE\n" +
                "WHEN sum(COALESCE(i.usage_denominator,0)) > 0 THEN round(sum(COALESCE(i.vaccinated,0))::numeric / sum(COALESCE(i.usage_denominator,0))::numeric, 4) * 100::numeric\n" +
                "ELSE NULL::numeric\n" +
                "END AS usage_rate,\n" +
                "CASE\n" +
                "WHEN sum(COALESCE(i.usage_denominator,0)) > 0 THEN 100::numeric - round(sum(COALESCE(i.vaccinated,0))::numeric / sum(COALESCE(i.usage_denominator,0))::numeric, 4) * 100::numeric\n" +
                "ELSE NULL::numeric\n" +
                "END AS wastage_rate\n" +
                "FROM\n" +
                "vw_vaccine_stock_status i\n" +
                "JOIN vw_districts d ON i.geographic_zone_id = d.district_id\n" +
                "JOIN vaccine_reports vr ON i.report_id = vr. ID\n" +
                "JOIN program_products pp ON pp.programid = vr.programid\n" +
                "AND pp.productid = i.product_id\n" +
                "JOIN product_categories pg ON pp.productcategoryid = pg. ID\n" +
                "WHERE\n" +
                "i.program_id = ( SELECT id FROM programs p WHERE p .enableivdform = TRUE )\n" +
                "AND i.period_start_date >= '2015-01-01' and i.period_end_date <= '2015-12-31'\n" +
                "group by 1,2,3\n" +
                "ORDER BY\n" +
                "i.product_name,\n" +
                "i.period_name")
        List<HashMap<String, Object>> getMonthlyWastage();

        @Select("SELECT\n" +
                "d.region_name,\n" +
                "d.district_name,\n" +
                "i.period_name,\n" +
                "i.product_name,\n" +
                "i.period_start_date, \n" +
                "CASE\n" +
                "WHEN sum(COALESCE(i.usage_denominator,0)) > 0 THEN round(sum(COALESCE(i.vaccinated,0))::numeric / sum(COALESCE(i.usage_denominator,0))::numeric, 4) * 100::numeric\n" +
                "ELSE NULL::numeric\n" +
                "END AS usage_rate,\n" +
                "CASE\n" +
                "WHEN sum(COALESCE(i.usage_denominator,0)) > 0 THEN 100::numeric - round(sum(COALESCE(i.vaccinated,0))::numeric / sum(COALESCE(i.usage_denominator,0))::numeric, 4) * 100::numeric\n" +
                "ELSE NULL::numeric\n" +
                "END AS wastage_rate\n" +
                "FROM\n" +
                "vw_vaccine_stock_status i\n" +
                "JOIN vw_districts d ON i.geographic_zone_id = d.district_id\n" +
                "JOIN vaccine_reports vr ON i.report_id = vr. ID\n" +
                "JOIN program_products pp ON pp.programid = vr.programid\n" +
                "AND pp.productid = i.product_id\n" +
                "JOIN product_categories pg ON pp.productcategoryid = pg. ID\n" +
                "WHERE\n" +
                "i.program_id = ( SELECT id FROM programs p WHERE p .enableivdform = TRUE )\n" +
                "--AND i.period_start_date >= '2015-01-01' and i.period_end_date <= '2015-12-31'\n" +
                "and i.period_id = 51\n" +
                "group by 1,2,3,4,5\n" +
                "ORDER BY\n" +
                "d.region_name,\n" +
                "d.district_name,\n" +
                "i.period_start_date")
        List<HashMap<String, Object>> getWastageByDistrict();


}
