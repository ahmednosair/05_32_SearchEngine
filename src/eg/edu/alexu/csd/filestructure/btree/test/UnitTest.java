package eg.edu.alexu.csd.filestructure.btree.test;

import eg.edu.alexu.csd.filestructure.btree.*;
import org.junit.Assert;
import org.junit.Test;

import javax.management.RuntimeErrorException;
import java.util.*;


public class UnitTest {
    private final boolean debug = false;

    /**
     * test get a null root.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testRootNull() {

        IBTree<String, String> btree = (IBTree<String, String>) TestRunner.getImplementationInstanceForInterface(IBTree.class, new Object[]{3});
        IBTreeNode<String, String> root = null;

        try {
            root = btree.getRoot();
            if (debug)
                System.out.println("TestRootNull: (case null)");
            if (root != null)
                Assert.fail();
        } catch (RuntimeErrorException ex) {
            if (debug)
                System.out.println("TestRootNull: (case runtime exception)");
        } catch (Throwable e) {
            TestRunner.fail("Fail to getRoot of tree", e);
        }
    }

    /**
     * Test get the root of the tree.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetRoot() {

        IBTree<String, String> btree = (IBTree<String, String>) TestRunner.getImplementationInstanceForInterface(IBTree.class, new Object[]{3});
        IBTreeNode<String, String> root = null;

        try {
            btree.insert("Soso", "Toto");
            root = btree.getRoot();
            Assert.assertEquals(1, root.getNumOfKeys());
            Assert.assertEquals("Soso", root.getKeys().get(0));
            Assert.assertEquals("Toto", root.getValues().get(0));
        } catch (Throwable e) {
            TestRunner.fail("Fail to getRoot of tree", e);
        }
    }

    /**
     * Test get the root of the tree full case.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetRootFull() {

        IBTree<String, String> btree = (IBTree<String, String>) TestRunner.getImplementationInstanceForInterface(IBTree.class, new Object[]{3});
        IBTreeNode<String, String> root = null;

        try {
            btree.insert("Soso0", "Toto0");
            btree.insert("Soso2", "Toto2");
            btree.insert("Soso4", "Toto4");
            btree.insert("Soso3", "Toto3");
            btree.insert("Soso1", "Toto1");
            root = btree.getRoot();
            Assert.assertEquals(5, root.getNumOfKeys());
            for (int i = 0; i < root.getNumOfKeys(); i++) {
                Assert.assertEquals("Soso" + i, root.getKeys().get(i));
                Assert.assertEquals("Toto" + i, root.getValues().get(i));
            }
            Assert.assertTrue(root.isLeaf());
            if (root.getChildren() != null)
                Assert.assertEquals(0, root.getChildren().size());
        } catch (Throwable e) {
            TestRunner.fail("Fail to getRoot of tree", e);
        }
    }

    /**
     * Test get the root of the tree split case.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetRootSplitCase() {

        IBTree<String, String> btree = (IBTree<String, String>) TestRunner.getImplementationInstanceForInterface(IBTree.class, new Object[]{3});
        IBTreeNode<String, String> root = null;

        try {
            btree.insert("Soso0", "Toto0");
            btree.insert("Soso2", "Toto2");
            btree.insert("Soso4", "Toto4");
            btree.insert("Soso5", "Toto5");
            btree.insert("Soso3", "Toto3");
            btree.insert("Soso1", "Toto1");
            root = btree.getRoot();
            Assert.assertEquals(1, root.getNumOfKeys());
            Assert.assertEquals("Soso3", root.getKeys().get(0));
            Assert.assertEquals("Toto3", root.getValues().get(0));

            IBTreeNode<String, String> left = root.getChildren().get(0);
            IBTreeNode<String, String> right = root.getChildren().get(1);

            Assert.assertEquals(3, left.getNumOfKeys());
            Assert.assertEquals(2, right.getNumOfKeys());

            for (int i = 0; i < left.getNumOfKeys(); i++) {
                Assert.assertEquals("Soso" + i, left.getKeys().get(i));
                Assert.assertEquals("Toto" + i, left.getValues().get(i));
            }

            for (int i = 0; i < right.getNumOfKeys(); i++) {
                Assert.assertEquals("Soso" + (i + 4), right.getKeys().get(i));
                Assert.assertEquals("Toto" + (i + 4), right.getValues().get(i));
            }

            Assert.assertFalse(root.isLeaf());
            Assert.assertTrue(left.isLeaf());
            Assert.assertTrue(right.isLeaf());
        } catch (Throwable e) {
            TestRunner.fail("Fail to getRoot of tree", e);
        }
    }

    /**
     * Test get the minimum degree of the tree invalid case.
     */
    @Test
    public void testGetMinimumDegreeInvalid() {

        try {
            TestRunner.getImplementationInstanceForInterface(IBTree.class, new Object[]{0});
            Assert.fail();
        } catch (RuntimeErrorException ex) {
            try {
                TestRunner.getImplementationInstanceForInterface(IBTree.class, new Object[]{1});
                Assert.fail();
            } catch (RuntimeErrorException ex1) {

            }
        } catch (Throwable e) {
            TestRunner.fail("Fail to getMinimumDegree", e);
        }
    }

    /**
     * Test get the minimum degree of the tree.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetMinimumDegree() {

        try {
            for (int i = 2; i < 10; i++) {
                IBTree<String, String> btree = (IBTree<String, String>) TestRunner.getImplementationInstanceForInterface(IBTree.class, new Object[]{i});
                Assert.assertEquals(i, btree.getMinimumDegree());
            }
        } catch (Throwable e) {
            TestRunner.fail("Fail to getMinimumDegree", e);
        }
    }


    /**
     * Test insertion with invalid parameter.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testInsertionInvalid() {

        IBTree<String, String> btree = (IBTree<String, String>) TestRunner.getImplementationInstanceForInterface(IBTree.class, new Object[]{3});

        try {
            btree.insert(null, "Soso");
            Assert.fail();
        } catch (RuntimeErrorException ex) {
            try {
                btree.insert("Koko", null);
                Assert.fail();
            } catch (RuntimeErrorException ex1) {
            }
        } catch (Throwable e) {
            TestRunner.fail("Fail to insert int tree", e);
        }
    }

    /**
     * Test insertion simple case.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testInsertionSimple() {

        IBTree<Integer, String> btree = (IBTree<Integer, String>) TestRunner.getImplementationInstanceForInterface(IBTree.class, new Object[]{5});

        try {
            Random r = new Random();
            List<Integer> rndNum = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                int rnd = r.nextInt(Integer.MAX_VALUE);
                btree.insert(rnd, "Toto" + rnd);
                rndNum.add(rnd);
            }
            IBTreeNode<Integer, String> root = btree.getRoot();
            Assert.assertTrue(root.isLeaf());
            Assert.assertEquals(9, root.getNumOfKeys());
            Collections.sort(rndNum);

            for (int i = 0; i < root.getNumOfKeys(); i++) {
                Assert.assertEquals(rndNum.get(i), root.getKeys().get(i));
                Assert.assertEquals("Toto" + rndNum.get(i), root.getValues().get(i));
            }

            if (!verifyBTree(root, 0, getHeight(root), 5, root))
                Assert.fail();

        } catch (Throwable e) {
            TestRunner.fail("Fail to insert in tree", e);
        }
    }

    /**
     * Test insertion duplicate case.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testInsertionDuplicate() {

        IBTree<Integer, String> btree = (IBTree<Integer, String>) TestRunner.getImplementationInstanceForInterface(IBTree.class, new Object[]{5});

        try {
            btree.insert(3, "Soso2");
            for (int i = 0; i < 3; i++) {
                btree.insert(3, "Toto" + 2);
            }
            IBTreeNode<Integer, String> root = btree.getRoot();
            Assert.assertTrue(root.isLeaf());
            Assert.assertEquals(1, root.getNumOfKeys());

            Assert.assertEquals(3, root.getKeys().get(0).intValue());
            Assert.assertEquals("Soso2", root.getValues().get(0));

            if (!verifyBTree(root, 0, getHeight(root), 5, root))
                Assert.fail();

        } catch (Throwable e) {
            TestRunner.fail("Fail to insert in tree", e);
        }
    }

    /**
     * Test insertion complex case 1.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testInsertionComplex1() {

        IBTree<Integer, String> btree = (IBTree<Integer, String>) TestRunner.getImplementationInstanceForInterface(IBTree.class, new Object[]{3});

        try {
            List<Integer> inp = Arrays.asList(new Integer[]{1, 3, 7, 10, 11, 13, 14, 15, 18, 16, 19, 24, 25, 26, 21, 4, 5, 20, 22, 2, 17, 12, 6});
            for (int i : inp)
                btree.insert(i, "Soso" + i);

            IBTreeNode<Integer, String> root = btree.getRoot();
            List<List<List<?>>> keys = new ArrayList<>();
            traverseBtreePreOrder(root, 0, keys);
            List<List<List<?>>> ans = new ArrayList<>();
            List<List<?>> lvl0 = new ArrayList<>();
            lvl0.add(new ArrayList<>(Arrays.asList(new Integer[]{16})));
            List<List<?>> lvl1 = new ArrayList<>();
            lvl1.add(new ArrayList<>(Arrays.asList(new Integer[]{3, 7, 13})));
            lvl1.add(new ArrayList<>(Arrays.asList(new Integer[]{20, 24})));
            List<List<?>> lvl2 = new ArrayList<>();
            lvl2.add(new ArrayList<>(Arrays.asList(new Integer[]{1, 2})));
            lvl2.add(new ArrayList<>(Arrays.asList(new Integer[]{4, 5, 6})));
            lvl2.add(new ArrayList<>(Arrays.asList(new Integer[]{10, 11, 12})));
            lvl2.add(new ArrayList<>(Arrays.asList(new Integer[]{14, 15})));
            lvl2.add(new ArrayList<>(Arrays.asList(new Integer[]{17, 18, 19})));
            lvl2.add(new ArrayList<>(Arrays.asList(new Integer[]{21, 22})));
            lvl2.add(new ArrayList<>(Arrays.asList(new Integer[]{25, 26})));
            ans.add(lvl0);
            ans.add(lvl1);
            ans.add(lvl2);
            for (int i = 0; i < keys.size(); i++) {
                for (int j = 0; j < keys.get(i).size(); j++) {
                    Assert.assertEquals(ans.get(i).get(j), keys.get(i).get(j));
                }
            }
            if (!verifyBTree(root, 0, getHeight(root), 3, root))
                Assert.fail();

        } catch (Throwable e) {
            TestRunner.fail("Fail to insert in tree", e);
        }
    }


    /**
     * Test insertion complex case 2.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testInsertionComplex2() {

        IBTree<Integer, String> btree = (IBTree<Integer, String>) TestRunner.getImplementationInstanceForInterface(IBTree.class, new Object[]{3});

        try {
            Random r = new Random();
            TreeSet<Integer> set = new TreeSet<>();
            for (int i = 0; i < 1000000; i++) {
                int key = r.nextInt(Integer.MAX_VALUE);
                btree.insert(key, "Soso" + key);
                set.add(key);
            }
            List<Integer> keys = new ArrayList<>();
            List<String> vals = new ArrayList<>();
            traverseTreeInorder(btree.getRoot(), keys, vals);
            if (keys.size() != set.size())
                Assert.fail();

            int index = 0;
            for (Integer i : set) {
                Assert.assertEquals(i, keys.get(index));
                Assert.assertEquals("Soso" + i, vals.get(index));
                index++;
            }
            if (!verifyBTree(btree.getRoot(), 0, getHeight(btree.getRoot()), 3, btree.getRoot()))
                Assert.fail();

        } catch (Throwable e) {
            TestRunner.fail("Fail to search in tree", e);
        }
    }


    /**
     * Test insertion complex case 3.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testInsertionComplex3() {

        IBTree<Integer, String> btree = (IBTree<Integer, String>) TestRunner.getImplementationInstanceForInterface(IBTree.class, new Object[]{10});

        try {
            Random r = new Random();
            TreeSet<Integer> set = new TreeSet<>();
            for (int i = 0; i < 1000000; i++) {
                int key = r.nextInt(Integer.MAX_VALUE);
                btree.insert(key, "Soso" + key);
                set.add(key);
            }
            List<Integer> keys = new ArrayList<>();
            List<String> vals = new ArrayList<>();
            traverseTreeInorder(btree.getRoot(), keys, vals);
            if (keys.size() != set.size())
                Assert.fail();

            int index = 0;
            for (Integer i : set) {
                Assert.assertEquals(i, keys.get(index));
                Assert.assertEquals("Soso" + i, vals.get(index));
                index++;
            }
            if (!verifyBTree(btree.getRoot(), 0, getHeight(btree.getRoot()), 10, btree.getRoot()))
                Assert.fail();
        } catch (Throwable e) {
            TestRunner.fail("Fail to search in tree", e);
        }
    }

    /**
     * Test search with invalid parameters.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testSearchInvalid() {
        IBTree<String, String> btree = (IBTree<String, String>) TestRunner.getImplementationInstanceForInterface(IBTree.class, new Object[]{3});

        try {
            btree.search(null);
            Assert.fail();
        } catch (RuntimeErrorException ex) {
        } catch (Throwable e) {
            TestRunner.fail("Fail to search in tree", e);
        }
    }

    /**
     * Test search simple case.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testSearchSimple() {

        IBTree<Integer, String> btree = (IBTree<Integer, String>) TestRunner.getImplementationInstanceForInterface(IBTree.class, new Object[]{5});

        try {
            Random r = new Random();
            List<Integer> rndNum = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                int rnd = r.nextInt(Integer.MAX_VALUE);
                btree.insert(rnd, "Lolo" + rnd);
                rndNum.add(rnd);
            }

            for (int i = 0; i < rndNum.size(); i++) {
                int searchKey = rndNum.get(i);
                Assert.assertEquals("Lolo" + searchKey, btree.search(searchKey));
            }
        } catch (Throwable e) {
            TestRunner.fail("Fail to search in tree", e);
        }
    }

    /**
     * Test search simple case 2.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testSearchSimple2() {

        IBTree<Integer, String> btree = (IBTree<Integer, String>) TestRunner.getImplementationInstanceForInterface(IBTree.class, new Object[]{3});

        try {
            List<Integer> inp = Arrays.asList(new Integer[]{1, 3, 7, 10, 11, 13, 14, 15, 18, 16, 19, 24, 25, 26, 21, 4, 5, 20, 22, 2, 17, 12, 6});
            for (int i : inp)
                btree.insert(i, "Soso" + i);

            for (int i = 0; i < inp.size(); i++) {
                int searchKey = inp.get(i);
                Assert.assertEquals("Soso" + searchKey, btree.search(searchKey));
            }

        } catch (Throwable e) {
            TestRunner.fail("Fail to search in tree", e);
        }
    }

    /**
     * Test search duplicate case.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testSearchDuplicate() {

        IBTree<Integer, String> btree = (IBTree<Integer, String>) TestRunner.getImplementationInstanceForInterface(IBTree.class, new Object[]{5});

        try {
            btree.insert(3, "Soso2");
            Assert.assertEquals("Soso2", btree.search(3));
            for (int i = 0; i < 3; i++) {
                btree.insert(3, "Toto" + 2);
            }
            Assert.assertEquals("Soso2", btree.search(3));

        } catch (Throwable e) {
            TestRunner.fail("Fail to search in tree", e);
        }
    }

    /**
     * Test search modify value.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testSearchModifyValue() {

        IBTree<Integer, List<String>> btree = (IBTree<Integer, List<String>>) TestRunner.getImplementationInstanceForInterface(IBTree.class, new Object[]{5});

        try {
            List<String> val = new ArrayList<>();
            val.add("koko");
            val.add("toto");
            btree.insert(3, val);
            List<String> ans = btree.search(3);
            Assert.assertEquals(val.size(), ans.size());
            for (int i = 0; i < ans.size(); i++)
                Assert.assertEquals(val.get(i), ans.get(i));
            val.add("Lolo");
            ans.add("Lolo");
            ans = btree.search(3);
            Assert.assertEquals(val.size(), ans.size());
            for (int i = 0; i < ans.size(); i++)
                Assert.assertEquals(val.get(i), ans.get(i));
        } catch (Throwable e) {
            TestRunner.fail("Fail to search in tree", e);
        }
    }

    /**
     * Test search not found.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testSearchNotFound() {

        IBTree<Integer, String> btree = (IBTree<Integer, String>) TestRunner.getImplementationInstanceForInterface(IBTree.class, new Object[]{3});

        try {
            List<Integer> inp = Arrays.asList(new Integer[]{1, 3, 7, 10, 11, 13, 14, 15, 18, 16, 19, 24, 25, 26, 21, 4, 5, 20, 22, 2, 17, 12, 6});
            for (int i : inp)
                btree.insert(i, "Soso" + i);

            Assert.assertNull(btree.search(23));
            Assert.assertNull(btree.search(0));
            Assert.assertNull(btree.search(400));
        } catch (Throwable e) {
            TestRunner.fail("Fail to search in tree", e);
        }
    }

    /**
     * Test deletion with invalid parameter.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDeletionInvalid() {

        IBTree<String, String> btree = (IBTree<String, String>) TestRunner.getImplementationInstanceForInterface(IBTree.class, new Object[]{3});

        try {
            btree.delete(null);
            Assert.fail();
        } catch (RuntimeErrorException ex) {
        } catch (Throwable e) {
            TestRunner.fail("Fail to delete int tree", e);
        }
    }

    /**
     * Test deletion simple case.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDeletionSimple() {

        IBTree<Integer, String> btree = (IBTree<Integer, String>) TestRunner.getImplementationInstanceForInterface(IBTree.class, new Object[]{5});

        try {
            Random r = new Random();
            List<Integer> rndNum = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                int rnd = r.nextInt(Integer.MAX_VALUE);
                btree.insert(rnd, "Toto" + rnd);
                rndNum.add(rnd);
            }
            IBTreeNode<Integer, String> root = btree.getRoot();
            Assert.assertTrue(root.isLeaf());
            Assert.assertEquals(9, root.getNumOfKeys());
            Collections.shuffle(rndNum);

            for (int i = 0; i < rndNum.size(); i++) {
                Assert.assertTrue(btree.delete(rndNum.get(i)));
                Assert.assertTrue(verifyBTree(root, 0, getHeight(root), 5, root));
            }


        } catch (Throwable e) {
            TestRunner.fail("Fail to delete in tree", e);
        }
    }

    /**
     * Test deletion complex case 1.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDeletionComplex1() {

        IBTree<Integer, String> btree = (IBTree<Integer, String>) TestRunner.getImplementationInstanceForInterface(IBTree.class, new Object[]{3});

        try {
            List<Integer> inp = Arrays.asList(new Integer[]{1, 3, 7, 10, 11, 13, 14, 15, 18, 16, 19, 24, 25, 26, 21, 4, 5, 20, 22, 2, 17, 12, 6});
            for (int i : inp)
                btree.insert(i, "Soso" + i);

            Collections.shuffle(inp);
            for (int i : inp) {
                Assert.assertTrue(btree.delete(i));
                if (btree.getRoot() != null)
                    verifyBTree(btree.getRoot(), 0, getHeight(btree.getRoot()), 3, btree.getRoot());
            }

        } catch (Throwable e) {
            TestRunner.fail("Fail to delete in tree", e);
        }
    }

    /**
     * Test deletion complex case 1.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDeletionComplex2() {

        IBTree<Integer, String> btree = (IBTree<Integer, String>) TestRunner.getImplementationInstanceForInterface(IBTree.class, new Object[]{3});

        try {
            List<Integer> inp = Arrays.asList(new Integer[]{1, 3, 7, 10, 11, 13, 14, 15, 18, 16, 19, 24, 25, 26, 21, 4, 5, 20, 22, 2, 17, 12, 6});
            for (int i : inp)
                btree.insert(i, "Soso" + i);

            Assert.assertTrue(btree.delete(6));
            Assert.assertTrue(verifyBTree(btree.getRoot(), 0, getHeight(btree.getRoot()), 3, btree.getRoot()));

            Assert.assertTrue(btree.delete(13));
            Assert.assertTrue(verifyBTree(btree.getRoot(), 0, getHeight(btree.getRoot()), 3, btree.getRoot()));

            Assert.assertTrue(btree.delete(7));
            Assert.assertTrue(verifyBTree(btree.getRoot(), 0, getHeight(btree.getRoot()), 3, btree.getRoot()));

            Assert.assertTrue(btree.delete(4));
            Assert.assertTrue(verifyBTree(btree.getRoot(), 0, getHeight(btree.getRoot()), 3, btree.getRoot()));

            Assert.assertTrue(btree.delete(2));
            Assert.assertTrue(verifyBTree(btree.getRoot(), 0, getHeight(btree.getRoot()), 3, btree.getRoot()));

            Assert.assertTrue(btree.delete(16));
            Assert.assertTrue(verifyBTree(btree.getRoot(), 0, getHeight(btree.getRoot()), 3, btree.getRoot()));

        } catch (Throwable e) {
            TestRunner.fail("Fail to delete in tree", e);
        }
    }

    /**
     * Test deletion complex case 3.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDeletionComplex3() {

        IBTree<Integer, String> btree = (IBTree<Integer, String>) TestRunner.getImplementationInstanceForInterface(IBTree.class, new Object[]{3});

        try {
            Random r = new Random();
            TreeSet<Integer> set = new TreeSet<>();
            HashSet<Integer> deleteSet = new HashSet<>();
            for (int i = 0; i < 100000; i++) {
                int key = r.nextInt(Integer.MAX_VALUE);
                btree.insert(key, "Soso" + key);
                set.add(key);
                if (r.nextInt(5) % 4 == 0) deleteSet.add(key);
            }
            List<Integer> keys = new ArrayList<>();
            List<String> vals = new ArrayList<>();
            traverseTreeInorder(btree.getRoot(), keys, vals);
            if (keys.size() != set.size())
                Assert.fail();

            for (Integer i : deleteSet) {
                Assert.assertTrue(btree.delete(i));
                Assert.assertNull(btree.search(i));
            }
            if (!verifyBTree(btree.getRoot(), 0, getHeight(btree.getRoot()), 3, btree.getRoot()))
                Assert.fail();

        } catch (Throwable e) {
            TestRunner.fail("Fail to search in tree", e);
        }
    }

    /**
     * Test deletion not found.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testDeletionNotFound() {

        IBTree<Integer, String> btree = (IBTree<Integer, String>) TestRunner.getImplementationInstanceForInterface(IBTree.class, new Object[]{3});

        try {
            List<Integer> inp = Arrays.asList(new Integer[]{1, 3, 7, 10, 11, 13, 14, 15, 18, 16, 19, 24, 25, 26, 21, 4, 5, 20, 22, 2, 17, 12, 6});
            for (int i : inp)
                btree.insert(i, "Soso" + i);

            Assert.assertFalse(btree.delete(23));
            Assert.assertFalse(btree.delete(0));
            Assert.assertFalse(btree.delete(400));
        } catch (Throwable e) {
            TestRunner.fail("Fail to delete in tree", e);
        }
    }


    /**
     * Test index web page with null or empty parameter or not found file.
     */
    @Test
    public void testindexWebPageNullorEmptyorNotFoundParamter() {

        ISearchEngine searchEngine = (ISearchEngine) TestRunner.getImplementationInstanceForInterface(ISearchEngine.class, new Object[]{100});

        try {
            searchEngine.indexWebPage(null);
            Assert.fail();
        } catch (RuntimeErrorException ex) {
            try {
                searchEngine.indexWebPage("");
                Assert.fail();
            } catch (RuntimeErrorException ex1) {
                try {
                    searchEngine.indexWebPage("koko");
                } catch (RuntimeErrorException ex2) {
                }
            }
        } catch (Throwable e) {
            TestRunner.fail("Fail to index web page", e);
        }
    }

    /**
     * Test index web page.
     */
    @Test
    public void testindexWebPage() {

        ISearchEngine searchEngine = (ISearchEngine) TestRunner.getImplementationInstanceForInterface(ISearchEngine.class, new Object[]{100});
        try {
            searchEngine.indexWebPage("wiki_samples/wiki_00.xml");
            String doesNotExistTestWord = "DoesNotExistWord";
            String existsTestWord = "overseas";
            String caseInsensitiveTestWord = "Overseas";
            List<ISearchResult> existsExpected = Arrays.asList(new SearchResult[]{new SearchResult("7700288", 1), new SearchResult("7701711", 1), new SearchResult("7700160", 2), new SearchResult("7702746", 5)});
            List<ISearchResult> actual;

            /**Testing a word that doesn't exist*/
            actual = searchEngine.searchByWordWithRanking(doesNotExistTestWord);
            Assert.assertEquals(0, actual.size());


            /**Testing a word that exists*/
            actual = searchEngine.searchByWordWithRanking(existsTestWord);
            actual.sort(((o1, o2) -> (o1.getRank() == o2.getRank()) ? o1.getId().compareTo(o2.getId()) : Integer.compare(o1.getRank(), o2.getRank())));
            Assert.assertEquals(existsExpected, actual);

            /**Testing the case insensitivity*/
            actual = searchEngine.searchByWordWithRanking(caseInsensitiveTestWord);
            actual.sort(((o1, o2) -> (o1.getRank() == o2.getRank()) ? o1.getId().compareTo(o2.getId()) : Integer.compare(o1.getRank(), o2.getRank())));
            Assert.assertEquals(existsExpected, actual);


        } catch (Throwable e) {
            TestRunner.fail("Fail to index web page", e);
        }
    }


    /**
     * Test index web directory with null or empty parameter or not found directory.
     */
    @Test
    public void testindexWebDirectoryNullorEmptyorNotFoundParamter() {

        ISearchEngine searchEngine = (ISearchEngine) TestRunner.getImplementationInstanceForInterface(ISearchEngine.class, new Object[]{100});

        try {
            searchEngine.indexDirectory(null);
            Assert.fail();
        } catch (RuntimeErrorException ex) {
            try {
                searchEngine.indexDirectory("");
                Assert.fail();
            } catch (RuntimeErrorException ex1) {
                try {
                    searchEngine.indexDirectory("koko");
                } catch (RuntimeErrorException ex2) {
                }
            }
        } catch (Throwable e) {
            TestRunner.fail("Fail to index directory", e);
        }
    }

    /**
     * Test index directory.
     */
    @Test
    public void testindexDirectorySimple() {

        ISearchEngine searchEngine = (ISearchEngine) TestRunner.getImplementationInstanceForInterface(ISearchEngine.class, new Object[]{100});
        try {
            searchEngine.indexDirectory("wiki_samples");
            String doesNotExistTestWord = "DoesNotExistWord";
            String existsTestWord = "CsedTestWord";
            String caseInsensitiveTestWord = "CSedTeStWoRd";
            List<ISearchResult> existsExpected = Arrays.asList(new SearchResult[]{new SearchResult("7702893", 1), new SearchResult("7702996", 2), new SearchResult("7715915", 3), new SearchResult("7724108", 4), new SearchResult("7734142", 5), new SearchResult("7742229", 6)});
            List<ISearchResult> actual;

            /**Testing a word that doesn't exist*/
            actual = searchEngine.searchByWordWithRanking(doesNotExistTestWord);
            Assert.assertEquals(0, actual.size());


            /**Testing a word that exists*/
            actual = searchEngine.searchByWordWithRanking(existsTestWord);
            actual.sort(((o1, o2) -> (o1.getRank() == o2.getRank()) ? o1.getId().compareTo(o2.getId()) : Integer.compare(o1.getRank(), o2.getRank())));
            Assert.assertEquals(existsExpected, actual);


            /**Testing the case insensitivity*/
            actual = searchEngine.searchByWordWithRanking(caseInsensitiveTestWord);
            actual.sort(((o1, o2) -> (o1.getRank() == o2.getRank()) ? o1.getId().compareTo(o2.getId()) : Integer.compare(o1.getRank(), o2.getRank())));
            Assert.assertEquals(existsExpected, actual);

        } catch (Throwable e) {
            TestRunner.fail("Fail to index directory", e);
        }

    }

    /**
     * Test index directory complex.
     */
    @Test
    public void testindexWebDirectoryComplex() {

        ISearchEngine searchEngine = (ISearchEngine) TestRunner.getImplementationInstanceForInterface(ISearchEngine.class, new Object[]{100});
        try {
            searchEngine.indexDirectory("wiki_samples");
            String doesNotExistTestWord = "DoesNotExistWordComplex";
            String existsInOuterTestWord = "CsedTestWordInOuter";
            String existsInNestedTestWord = "CsedTestWordNested";
            String caseInsensitiveOuterTestWord = "csEdtesTwoRdiNoutEr";
            String caseInsensitiveNestedTestWord = "CsEdTesTWordNeStEd";
            String existsInTitleTestWord = "CsedTestWordInTitle";
            String caseInsensitiveInTitleTestWord = "CsEdTeStWordInTiTlE";
            List<ISearchResult> existsInOuterExpected = Arrays.asList(new SearchResult[]{new SearchResult("7712158", 1), new SearchResult("7724161", 2), new SearchResult("7734159", 4)});
            List<ISearchResult> existsInNestedExpected = Arrays.asList(new SearchResult[]{new SearchResult("7702805", 1), new SearchResult("7712160", 2), new SearchResult("7724107", 4)});
            List<ISearchResult> existsInTitleExpected = Arrays.asList(new SearchResult[]{new SearchResult("7708196", 1), new SearchResult("7734142", 2), new SearchResult("7742263", 4)});
            List<ISearchResult> actual;

            /**Testing a word that doesn't exist*/
            actual = searchEngine.searchByWordWithRanking(doesNotExistTestWord);
            Assert.assertEquals(0, actual.size());


            /**Testing a word that exists in files located at the most outer directory*/
            actual = searchEngine.searchByWordWithRanking(existsInOuterTestWord);
            actual.sort(((o1, o2) -> (o1.getRank() == o2.getRank()) ? o1.getId().compareTo(o2.getId()) : Integer.compare(o1.getRank(), o2.getRank())));
            Assert.assertEquals(existsInOuterExpected, actual);


            /**Testing a word that exists in files located at nested directories*/
            actual = searchEngine.searchByWordWithRanking(existsInNestedTestWord);
            actual.sort(((o1, o2) -> (o1.getRank() == o2.getRank()) ? o1.getId().compareTo(o2.getId()) : Integer.compare(o1.getRank(), o2.getRank())));
            Assert.assertEquals(existsInNestedExpected, actual);


            /**Testing a word that exists in the title of documents at the files*/
            actual = searchEngine.searchByWordWithRanking(existsInTitleTestWord);
            actual.sort(((o1, o2) -> (o1.getRank() == o2.getRank()) ? o1.getId().compareTo(o2.getId()) : Integer.compare(o1.getRank(), o2.getRank())));
            Assert.assertEquals(existsInTitleExpected, actual);


            /**Testing the case insensitivity in a word that exists in files located at the most outer directory*/
            actual = searchEngine.searchByWordWithRanking(caseInsensitiveOuterTestWord);
            actual.sort(((o1, o2) -> (o1.getRank() == o2.getRank()) ? o1.getId().compareTo(o2.getId()) : Integer.compare(o1.getRank(), o2.getRank())));
            Assert.assertEquals(existsInOuterExpected, actual);

            /**Testing the case insensitivity in a word that exists in files located at nested directories*/
            actual = searchEngine.searchByWordWithRanking(caseInsensitiveNestedTestWord);
            actual.sort(((o1, o2) -> (o1.getRank() == o2.getRank()) ? o1.getId().compareTo(o2.getId()) : Integer.compare(o1.getRank(), o2.getRank())));
            Assert.assertEquals(existsInNestedExpected, actual);

            /**Testing the case insensitivity in a word that exists in the title of documents at the files*/
            actual = searchEngine.searchByWordWithRanking(caseInsensitiveInTitleTestWord);
            actual.sort(((o1, o2) -> (o1.getRank() == o2.getRank()) ? o1.getId().compareTo(o2.getId()) : Integer.compare(o1.getRank(), o2.getRank())));
            Assert.assertEquals(existsInTitleExpected, actual);


        } catch (Throwable e) {
            TestRunner.fail("Fail to index directory", e);
        }
    }

    /**
     * Test delete web page with null or empty parameter or not found.
     */
    @Test
    public void testDeleteWebPageNullorEmptyorNotFoundParamter() {

        ISearchEngine searchEngine = (ISearchEngine) TestRunner.getImplementationInstanceForInterface(ISearchEngine.class, new Object[]{100});

        try {
            searchEngine.deleteWebPage(null);
            Assert.fail();
        } catch (RuntimeErrorException ex) {
            try {
                searchEngine.deleteWebPage("");
                Assert.fail();
            } catch (RuntimeErrorException ex1) {
                try {
                    searchEngine.deleteWebPage("koko");
                } catch (RuntimeErrorException ex2) {
                }
            }
        } catch (Throwable e) {
            TestRunner.fail("Fail to delete web page", e);
        }
    }

    /**
     * Test delete web page complex.
     */
    @Test
    public void testDeleteWebPageSimple() {

        ISearchEngine searchEngine = (ISearchEngine) TestRunner.getImplementationInstanceForInterface(ISearchEngine.class, new Object[]{100});
        try {
            searchEngine.indexDirectory("wiki_samples");
            String doesNotExistTestWord = "DoesNotExistWordComplex";
            String deleteTestWord = "DeleteWord";
            String deleteCaseInsensitiveTestWord = "DeLeTEWoRD";
            List<ISearchResult> expectedBefore = Arrays.asList(new SearchResult[]{new SearchResult("7702893", 1), new SearchResult("7708226", 2), new SearchResult("7712160", 3)});
            List<ISearchResult> expectedAfterOuterDelete = Arrays.asList(new SearchResult[]{new SearchResult("7708226", 2), new SearchResult("7712160", 3)});
            List<ISearchResult> expectedCaseInsensitiveAfterDelete = Arrays.asList(new SearchResult[]{new SearchResult("7712160", 3)});
            List<ISearchResult> actual;


            /**Testing a word that doesn't exist*/
            actual = searchEngine.searchByWordWithRanking(doesNotExistTestWord);
            Assert.assertEquals(0, actual.size());

            /**Testing before deletion*/
            actual = searchEngine.searchByWordWithRanking(deleteTestWord);
            actual.sort(((o1, o2) -> (o1.getRank() == o2.getRank()) ? o1.getId().compareTo(o2.getId()) : Integer.compare(o1.getRank(), o2.getRank())));
            Assert.assertEquals(expectedBefore, actual);


            /**Testing after deleting an outer file*/
            searchEngine.deleteWebPage("wiki_samples/wiki_01.xml");
            actual = searchEngine.searchByWordWithRanking(deleteTestWord);
            actual.sort(((o1, o2) -> (o1.getRank() == o2.getRank()) ? o1.getId().compareTo(o2.getId()) : Integer.compare(o1.getRank(), o2.getRank())));
            Assert.assertEquals(expectedAfterOuterDelete, actual);


            /**Testing after deleting an outer file verifying case insensitivity*/
            searchEngine.deleteWebPage("wiki_samples/wiki_02.xml");
            actual = searchEngine.searchByWordWithRanking(deleteCaseInsensitiveTestWord);
            actual.sort(((o1, o2) -> (o1.getRank() == o2.getRank()) ? o1.getId().compareTo(o2.getId()) : Integer.compare(o1.getRank(), o2.getRank())));
            Assert.assertEquals(expectedCaseInsensitiveAfterDelete, actual);


            /**Testing after deleting a nested file*/
            searchEngine.deleteWebPage("wiki_samples/nested/wiki_03.xml");
            actual = searchEngine.searchByWordWithRanking(deleteTestWord);
            actual.sort(((o1, o2) -> (o1.getRank() == o2.getRank()) ? o1.getId().compareTo(o2.getId()) : Integer.compare(o1.getRank(), o2.getRank())));
            Assert.assertTrue(actual.isEmpty());


        } catch (Throwable e) {
            TestRunner.fail("Fail to delete web page", e);
        }
    }

    /**
     * Test delete unindexed web page.
     */
    @Test
    public void testDeleteWebPageUnIndexedWebPage() {

        ISearchEngine searchEngine = (ISearchEngine) TestRunner.getImplementationInstanceForInterface(ISearchEngine.class, new Object[]{100});
        try {
            String deleteTestWord = "DeleteWord";
            List<ISearchResult> expected = Arrays.asList(new SearchResult[]{new SearchResult("7712160", 3)});
            List<ISearchResult> actual;

            /**Test deleting an unIndexed Web page*/
            searchEngine.indexWebPage("wiki_samples/wiki_00.xml");
            searchEngine.indexWebPage("wiki_samples/nested/wiki_03.xml");
            searchEngine.deleteWebPage("wiki_samples/wiki_01.xml");
            actual = searchEngine.searchByWordWithRanking(deleteTestWord);
            actual.sort(((o1, o2) -> (o1.getRank() == o2.getRank()) ? o1.getId().compareTo(o2.getId()) : Integer.compare(o1.getRank(), o2.getRank())));
            Assert.assertEquals(expected, actual);

        } catch (Throwable e) {
            TestRunner.fail("Fail to delete web page", e);
        }
    }

    /**
     * Test delete empty all the indexed web page.
     */
    @Test
    public void testDeleteAllIndexedWebPage() {

        ISearchEngine searchEngine = (ISearchEngine) TestRunner.getImplementationInstanceForInterface(ISearchEngine.class, new Object[]{100});

        try {
            searchEngine.indexWebPage("wiki_samples/wiki_00.xml");
            searchEngine.indexWebPage("wiki_samples/wiki_01.xml");
            searchEngine.indexWebPage("wiki_samples/nested/wiki_03.xml");
            searchEngine.deleteWebPage("wiki_samples/wiki_01.xml");
            searchEngine.deleteWebPage("wiki_samples/nested/wiki_03.xml");
            searchEngine.deleteWebPage("wiki_samples/wiki_00.xml");

            List<ISearchResult> actual = searchEngine.searchByWordWithRanking("ThE");
            Assert.assertEquals(0, actual.size());
        } catch (Throwable e) {
            TestRunner.fail("Fail to delete web page", e);
        }
    }

    /**
     * Test searchByWordWithRanking with null or empty parameter.
     */
    @Test
    public void testsearchByWordWithRankingNullorEmptyParamter() {

        ISearchEngine searchEngine = (ISearchEngine) TestRunner.getImplementationInstanceForInterface(ISearchEngine.class, new Object[]{100});

        try {
            searchEngine.searchByWordWithRanking(null);
            Assert.fail();
        } catch (RuntimeErrorException ex) {
            Assert.assertEquals(0, searchEngine.searchByWordWithRanking("").size());
        } catch (Throwable e) {
            TestRunner.fail("Fail to search web page", e);
        }
    }

    /**
     * Test searchByMultipleWordWithRanking with null or empty parameter.
     */
    @Test
    public void testsearchByMultipleWordWithRankingNullorEmptyParamter() {
        /**
         * The rest use case of searchByWordWithRanking are covered in the other tests.
         */
        ISearchEngine searchEngine = (ISearchEngine) TestRunner.getImplementationInstanceForInterface(ISearchEngine.class, new Object[]{100});

        try {
            searchEngine.searchByMultipleWordWithRanking(null);
            Assert.fail();
        } catch (RuntimeErrorException ex) {
            Assert.assertEquals(0, searchEngine.searchByMultipleWordWithRanking("").size());
        } catch (Throwable e) {
            TestRunner.fail("Fail to search web page", e);
        }
    }

    /**
     * Test searchByMultipleWordWithRanking cases.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testsearchByMultipleWordWithRanking() {

        ISearchEngine searchEngine = (ISearchEngine) TestRunner.getImplementationInstanceForInterface(ISearchEngine.class, new Object[]{100});

        try {
            String existingSentence = "CSED_22       TestMultipleWord     TestSearchRanking";
            String NotExistingSentence = "CSED_22       TestMultipleWord     VERyVERyBAdWORd";

            searchEngine.indexDirectory("wiki_samples");
            List<ISearchResult> existingExpected = Arrays.asList(new SearchResult[]{new SearchResult("7702685", 1), new SearchResult("7715915", 2), new SearchResult("7724177", 3)});
            List<ISearchResult> actual;


            /**Testing multiple words with different cases that exists in the tree.*/
            actual = searchEngine.searchByMultipleWordWithRanking(existingSentence);
            actual.sort(((o1, o2) -> (o1.getRank() == o2.getRank()) ? o1.getId().compareTo(o2.getId()) : Integer.compare(o1.getRank(), o2.getRank())));
            Assert.assertEquals(existingExpected, actual);


            /**Testing multiple words with some of them not in the tree.*/
            actual = searchEngine.searchByMultipleWordWithRanking(NotExistingSentence);
            Assert.assertTrue(actual.isEmpty());


        } catch (Throwable e) {
            TestRunner.fail("Fail to index directory", e);
        }
    }

    private int getHeight(IBTreeNode<?, ?> node) {
        if (node.isLeaf()) return 0;

        return node.getNumOfKeys() > 0 ? 1 + getHeight(node.getChildren().get(0)) : 0;
    }

    private boolean verifyBTree(IBTreeNode<?, ?> node, int lvl, int height, int t, IBTreeNode<?, ?> root) {
        if (!node.equals(root))
            if (node.getNumOfKeys() < t - 1 || node.getNumOfKeys() > 2 * t - 1)
                return false;
        boolean ans = true;
        if (!node.isLeaf()) {
            for (int i = 0; i <= node.getNumOfKeys(); i++) {
                ans = ans && verifyBTree(node.getChildren().get(i), lvl + 1, height, t, root);
                if (!ans) break;
            }

        } else {
            ans = ans && (lvl == height);
        }
        return ans;
    }

    private void traverseTreeInorder(IBTreeNode<Integer, String> node, List<Integer> keys, List<String> vals) {
        int i;
        for (i = 0; i < node.getNumOfKeys(); i++) {

            if (!node.isLeaf())
                traverseTreeInorder(node.getChildren().get(i), keys, vals);
            keys.add(node.getKeys().get(i));
            vals.add(node.getValues().get(i));
        }
        if (!node.isLeaf())
            traverseTreeInorder(node.getChildren().get(i), keys, vals);
    }

    private void traverseBtreePreOrder(IBTreeNode<?, ?> node, int level, List<List<List<?>>> keys) {
        if (level >= keys.size())
            keys.add(new ArrayList<>());
        keys.get(level).add(node.getKeys());
        if (!node.isLeaf())
            for (int j = 0; j <= node.getNumOfKeys(); j++)
                traverseBtreePreOrder(node.getChildren().get(j), level + 1, keys);
    }


}