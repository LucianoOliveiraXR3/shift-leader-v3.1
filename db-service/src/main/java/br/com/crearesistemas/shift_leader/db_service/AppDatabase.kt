package br.com.crearesistemas.shift_leader.db_service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.crearesistemas.shift_leader.db_service.dao.*
import br.com.crearesistemas.shift_leader.db_service.model.*
import org.intellij.lang.annotations.Language

@Database(
    entities = [
        // main entities
        Appointment::class,
        AppointmentGroup::class,
        AppointmentSaved::class,
        AppointmentSemiMechanized::class,
        Checklist::class,
        ChecklistSaved::class,
        Driver::class,
        DriverLogin::class,
        FarmCompartment::class,
        FarmSector::class,
        MachineActivityType::class,
        MachineEquipment::class,
        MachineMechanizationType::class,
        MachineSetup::class,
        ProductionHarvester::class,
        Telemetry::class,
        TelemetryMonitoring::class,
        TelemetryAlert::class,
        AppointmentData::class,
        AppointmentMachineType::class,
        MaintenanceTask::class,
        MachineGroup::class,

        // secondary entities
        User::class,
        Machine::class,
        TabletConfiguration::class,

        ShiftLeaderConfigMachine::class,
        ShiftLeaderConfig::class
    ],
    version = 9,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    // main dao

    abstract fun appointmentDao(): AppointmentDao

    abstract fun appointmentGroupDao(): AppointmentGroupDao

    abstract fun appointmentSavedDao(): AppointmentSavedDao

    abstract fun appointmentSemiMechanized(): AppointmentSemiMechanizedDao

    abstract fun checkListDao(): ChecklistDao

    abstract fun checkListSavedDao(): ChecklistSavedDao

    abstract fun driverDao(): DriverDao

    abstract fun driverLoginDaoDao(): DriverLoginDao

    abstract fun farmCompartmentDao(): FarmCompartmentDao

    abstract fun farmSectorDao(): FarmSectorDao

    abstract fun machineActivityTypeDao(): MachineActivityTypeDao

    abstract fun machineEquipmentDao(): MachineEquipmentDao

    abstract fun machineMechanizationTypeDao(): MachineMechanizationTypeDao

    abstract fun machineSetupDao(): MachineSetupDao

    abstract fun productionHarvesterDao(): ProductionHarvesterDao

    abstract fun telemetryDao(): TelemetryDao

    abstract fun telemetryMonitoringDao(): TelemetryMonitoringDao

    abstract fun telemetryAlertDao(): TelemetryAlertDao

    abstract fun appointmentDataDao(): AppointmentDataDao

    abstract fun appointmentMachineType(): AppointmentMachineTypeDao

    abstract fun maintenanceTaskDao(): MaintenanceTaskDao

    abstract fun machineGroupDao(): MachineGroupDao

    // secondary dao

    abstract fun userDao(): UserDao

    abstract fun machineDao(): MachineDao

    abstract fun tabletConfigurationDao(): TabletConfigurationDao

    abstract fun shiftLeaderConfigDao(): ShiftLeaderConfigDao
    abstract fun shiftLeaderConfigMachineDao(): ShiftLeaderConfigMachineDao

    companion object {

        private const val DB_NAME = "shift_leader.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {

            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            DB_NAME
                        )
                            .addMigrations(
                                MIGRATION_0_1,
                                MIGRATION_1_2,
                                MIGRATION_2_3,
                                MIGRATION_3_4,
                                MIGRATION_4_5,
                                MIGRATION_5_6,
                                MIGRATION_6_7,
                                MIGRATION_7_8,
                                MIGRATION_8_9,
                                MIGRATION_10,
                            )
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }


            return INSTANCE
        }

        // Migration from 0 to 1
        // https://sqlite.org/datatype3.html
        private val MIGRATION_0_1 = object : Migration(0, 1) {
            override fun migrate(database: SupportSQLiteDatabase) {

                @Language("SQL")
                val sqls = listOf(
                    """
                    ---------------------------------------------------------------------
                    -- Main Tables

                    --- Appointment
                    create table appointment_list (
                        code INTEGER,
                        origin TEXT,
                        group_code TEXT,
                        description_primary TEXT,
                        description_secondary TEXT,
                        repetitive NUMERIC,
                        collected_at TEXT,
                        primary key(code)
                    );
                """, """
                    --- AppointmentGroup
                    create table appointment_groups (
                        code INTEGER,
                        origin TEXT,
                        description_primary TEXT,
                        description_secondary TEXT,
                        collected_at TEXT,
                        primary key(code)
                    );
                """, """
                    --- AppointmentSaved
                    create table appointment_saved (
                        id                  INTEGER,
                        origin              TEXT,
                        appointment_code    INTEGER,
                        group_code          TEXT,
                        driver_id           INTEGER,                                                
                        telemetry_id        INTEGER,
                        data                TEXT,
                        sent_to_cloud       NUMERIC,
                        event_time          DATETIME,
                        collected_at        DATETIME,
                        primary key(id, origin)
                    );
                """, """
                    --- AppointmentSemiMechanized
                    create table appointment_semi_mechanized (
                    	id INTEGER AUTOINCREMENT,
                    	origin TEXT,
                    	user_id INTEGER,
                    	type TEXT,
                    	data TEXT,
                        sent_to_cloud NUMERIC,
                        collected_at TEXT,
                        primary key(id)
                    );
                """, """
                    --- Checklist
                    create table checklist_items (
                        id INTEGER,
                        origin TEXT,
                        equipment_id INTEGER,
                        description_primary TEXT,
                        description_secondary TEXT,
                        collected_at TEXT,
                        primary key(id)
                    );
                """, """
                    --- ChecklistSaved
                    create table checklist_saved (
                        id              INTEGER,
                        origin          TEXT,                                                
                        driver_id       INTEGER,                        
                        items           TEXT,
                        sent_to_cloud   NUMERIC,
                        event_time      DATETIME,
                        collected_at    DATETIME,
                        primary key(id, origin)
                    );
                """, """
                     --- Driver
                    create table driver_list (
                        id INTEGER,
                        origin TEXT,
                        badge_code TEXT,
                        name TEXT,                        
                        collected_at TEXT,
                        primary key(id)
                    );
                """, """
                     --- DriverLogin
                    create table driver_login (
                        driver_id       INTEGER     NOT NULL,
                        origin          TEXT        NOT NULL,
                        start_date      DATETIME    NOT NULL,                        
                        final_date      DATETIME    NOT NULL,
                        sent_to_cloud   NUMERIC,                                            
                        collected_at    DATETIME,
                        primary key(driver_id, start_date, origin)
                    );
                """, """
                     --- FarmCompartment
                    create table farm_compartment (
                        id INTEGER,
                        origin TEXT,
                        sector_id INTEGER,
                        description TEXT,                        
                        collected_at TEXT,
                        primary key(id)
                    );
                """, """
                     --- FarmSector
                    create table farm_sector (
                        id INTEGER,
                        origin TEXT,
                        description TEXT,                        
                        collected_at TEXT,
                        primary key(id)
                    );
                """, """
                    --- MachineActivityType
                    create table machine_activity_type (
                        id INTEGER,
                        origin TEXT,
                        description_primary TEXT,
                        description_secondary TEXT,
                        equipment_id INTEGER,                        
                        collected_at TEXT,
                        primary key(id)
                    );
                """, """
                     --- MachineEquipment
                    create table machine_equipment (
                        id INTEGER,
                        origin TEXT,
                        description_primary TEXT,
                        description_secondary TEXT,                        
                        collected_at TEXT,
                        primary key(id)
                    );
                """, """
                     --- MachineMechanizationType
                    create table machine_mechanization_type (
                        id INTEGER,
                        origin TEXT,
                        description_primary TEXT,
                        description_secondary TEXT,
                        equipment_id INTEGER,                        
                        collected_at TEXT,
                        primary key(id)
                    );
                """, """
                     --- MachineSetup
                    CREATE TABLE MACHINE_SETUP (
                        id                  INTEGER,
                        origin              TEXT,
                        driver_id           INTEGER,
                        mechanization_id    INTEGER,
                        activity_id         INTEGER,
                        compartment_id      INTEGER,                        
                        telemetry_id        INTEGER,
                        sent_to_cloud       NUMERIC,
                        event_time          DATETIME,
                        collected_at        DATETIME,
                        PRIMARY KEY (id, origin)
                    );
                """, """
                     --- ProductionHarvester
                    create table production_harvester (
                        insert_date     DATETIME,
                        origin          TEXT,                        
                        n_stems         INTEGER,
                        volume          FLOAT,
                        sent_to_cloud   NUMERIC,                        
                        collected_at    TEXT,
                        primary key     (insert_date, origin)
                    );
                """, """
                    --- Telemetry
                    create table telemetry_list (
                        id              INTEGER,
                        origin          TEXT,                                        
                        insert_date     DATETIME,
                        ignition        NUMERIC,
                        hourmeter       INTEGER,
                        hodometer INTEGER,
                        latitude REAL,
                        longitude REAL,
                        rpm INTEGER,
                        speed INTEGER,
                        fuel_level INTEGER,
                        fuel_total INTEGER,
                        sent_to_cloud NUMERIC,                                                
                        collected_at TEXT,
                        primary key(id, origin)
                    );
                """, """
                    --- TelemetryMonitoring
                    create table telemetry_monitoring (
                        pgn INTEGER,
                        origin TEXT,                                                                                                                                                                                     
                        description TEXT,
                        min INTEGER,
                        max INTEGER,                                                                      
                        collected_at TEXT,
                        primary key(pgn)
                    );
                """, """
                    --- TelemetryAlert
                    create table telemetry_alert (
                        pgn INTEGER,
                        event_time NUMERIC,
                        origin TEXT,   
                        value INTEGER,
                        sent_to_cloud NUMERIC,                                                                      
                        collected_at TEXT,
                        primary key(pgn, event_time, origin)
                    );
                """, """
                    ---------------------------------------------------------------------
                    -- Secondary Tables
                    
                    --- User
                    create table user_list (
                        id INTEGER,
                        username TEXT,
                        password TEXT,
                        roles TEXT,
                        collected_at TEXT,
                        primary key(id)
                    );
                """, """
                    --- Machine
                    create table machine_list (
                        id INTEGER,
                        name TEXT,
                        description TEXT,
                        hotspot_ssid TEXT,
                        hotspotPassword TEXT,
                        collected_at TEXT,
                        primary key(id)
                    );
                """, """
                     --- Tablet Configuration
                    create table tablet_configuration (
                        'key' TEXT,
                        value TEXT,
                        description TEXT,
                        received_at TEXT,
                        primary key('key')
                    );
                """, """
                    --- Shift Leader Config
                    create table shift_leader_config (
                        id INTEGER,
                        last_sync TEXT,
                        limit_yellow NUMERIC,
                        limit_red NUMERIC,
                        primary key(id)
                    );
                """, """
                    --- Shift Leader Config Machine
                    create table shift_leader_config_machine (
                        id INTEGER,
                        ssid TEXT,
                        password TEXT,
                        is_connected NUMERIC,
                        last_sync TEXT
                    );
                """
                )

                sqls.forEach { database.execSQL(it) }
            }
        }


        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {

                @Language("SQL")
                val sqls = listOf(
                    """
                        ---------------------------------------------------------------------
                        -- Main Tables
    
                        --- Appointment
                        alter table appointment_list add machine_id TEXT;
                    """, """
                        --- Checklist
                        alter table checklist_items add machine_id TEXT;
                    """, """
                        --View
                        create or replace view view_checklist_answers as
                        select 
                          cs.origin as origin, cs.id as checklist_id, cs.driver_id, cs.received_at, i.id as question_id, i.checked::varchar as answer
                        from public.checklist_saved cs
                          , json_to_recordset(cs.items::json) as i (checked varchar, id int)
                        where i.id in (select id from public.checklist_items);
                    """
                )

                sqls.forEach { database.execSQL(it) }
            }
        }
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                       create table appointment_data(
                       id                INTEGER, 
                       appointment_id    INTEGER,
                       name              TEXT,
                       type              INTEGER,
                       primary key(id)
                       );
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                       create table appointment_machine_type(
                       id                INTEGER, 
                       machine_type_id   INTEGER,
                       appointment_code  INTEGER,
                       primary key(id)
                       );
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                       alter table appointment_data  add optional  TEXT
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                       create table maintenance_tasks(
                       id                INTEGER, 
                       version           INTEGER,
                       type              TEXT,
                       task_date         TEXT,
                       primary key(id)
                       );
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {}
        }

        private val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                     create TABLE machine_group(
                      id                    BIGINT NOT NULL,
                      origin                VARCHAR,
                      description_primary   VARCHAR,
                      description_secondary VARCHAR,
                      received_at           TEXT,
                      primary key(id)
                    )
                    """.trimIndent()
                )
            }
        }


        private val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    ALTER TABLE machine_setup add machine_group_id BIGINT
                    """.trimIndent()
                )
            }
        }


        private val MIGRATION_10 = object : Migration(9,10){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    ALTER table appointment_list ADD update_at DATETIME;
                    ALTER table appointment_list ADD enabled boolean NOT NULL DEFAULT true;
                    
                    ALTER table appointment_groups ADD update_at DATETIME;
                    ALTER table appointment_groups ADD enabled boolean NOT NULL DEFAULT true;
                    
                    ALTER table driver_list ADD update_at DATETIME;
                    ALTER table driver_list ADD enabled boolean NOT NULL DEFAULT true;
                    
                    ALTER table farm_compartment ADD update_at DATETIME;
                    ALTER table farm_compartment ADD enabled boolean NOT NULL DEFAULT true;
                    
                    ALTER table farm_sector ADD update_at DATETIME;
                    ALTER table farm_sector ADD enabled boolean NOT NULL DEFAULT true;
                    
                    ALTER table machine_activity_type ADD update_at DATETIME;
                    ALTER table machine_activity_type ADD enabled boolean NOT NULL DEFAULT true;
                    
                    ALTER table machine_equipment ADD update_at DATETIME;
                    ALTER table machine_equipment ADD enabled boolean NOT NULL DEFAULT true;
                    
                    ALTER table machine_mechanization_type ADD update_at DATETIME;
                    ALTER table machine_mechanization_type ADD enabled boolean NOT NULL DEFAULT true;

                    """.trimIndent()
                )
            }
        }

    }
}
