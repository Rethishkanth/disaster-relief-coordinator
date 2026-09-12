package com.disasterrelief;

import com.disasterrelief.model.Disaster;
import com.disasterrelief.model.Shelter;
import com.disasterrelief.model.SystemSnapshot;
import com.disasterrelief.util.SerializationUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class SerializationUtilTest {

    @Test
    public void testSerializationCycle() throws Exception {
        SystemSnapshot snapshot = new SystemSnapshot("JUnit Automated Test Snapshot");
        snapshot.getDisasters().add(new Disaster(1, "Test Cyclone", "CYCLONE", "Coast", "CRITICAL", "ACTIVE", "Test event"));
        snapshot.getShelters().add(new Shelter(1, 1, 1, "Test Hall", "Main St", 500, 320, "+91 999", "OPERATIONAL"));

        // Save
        String savedFileName = SerializationUtil.saveSnapshot(snapshot);
        Assert.assertNotNull(savedFileName);
        Assert.assertTrue(savedFileName.endsWith(".ser"));

        // Verify file exists on disk
        File f = new File("snapshots" + File.separator + savedFileName);
        Assert.assertTrue("Snapshot file must exist on disk", f.exists());

        // Restore
        SystemSnapshot restored = SerializationUtil.loadSnapshot(savedFileName);
        Assert.assertNotNull(restored);
        Assert.assertEquals("JUnit Automated Test Snapshot", restored.getDescription());
        Assert.assertEquals(1, restored.getDisasters().size());
        Assert.assertEquals("Test Cyclone", restored.getDisasters().get(0).getName());
        Assert.assertEquals(1, restored.getShelters().size());
        Assert.assertEquals("Test Hall", restored.getShelters().get(0).getName());
    }
}
