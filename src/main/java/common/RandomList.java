package common;

import java.io.Serializable;
import java.util.*;

public class RandomList<T> implements Iterable<T>, Serializable {

    private static final long serialVersionId = 1L;

    private List<Integer> planSeqList = new ArrayList<>();
    private List<T> candidatePool = new ArrayList<>();
    private Map<T, Integer> planIndexMap = new HashMap<>();

    private int size = 0;
    private int candidatePoolSize = 0;
    private int candidatePoolStartIndex = 0;

    public RandomList() {
    }


    public RandomList(List<T> _planList) {
        this.ini(_planList);
    }

    public void showPlans() {

        System.out.println("Map info");
        for (Map.Entry entry : planIndexMap.entrySet()) {
            System.out.println("    key = " + entry.getKey() +", value = " + entry.getValue() );
        }


        System.out.println("candidate pool");
        for ( T plan : candidatePool) {
            System.out.println("    plan = " + plan.toString());
        }

        System.out.println("pool start index = " + candidatePoolStartIndex);
        for (int seq: planSeqList) {
            System.out.println("    seq = " + seq);
        }
    }

    public void ini(List<T> _planList) {

        //poolstartindex 이후부터 사용이 가능
        candidatePoolStartIndex = 0;
        candidatePoolSize = _planList.size();

        candidatePool.clear();
        planSeqList.clear();

        //candidate 추가
        candidatePool.addAll(_planList);

        // plan index map 생성
        int i = 0;
        for (T plan : _planList) {
            planIndexMap.put(plan, i);
            i++;
        }

        // plan seq list 생성
        for (T plan : _planList) {
            planSeqList.add(planIndexMap.get(plan));
        }

        //TODO 과연 필요한 로직일까? 의문
        //candidate pool과 sq의 순서를 맞춰준다.
        for (i = 0; i < candidatePool.size(); i++) {
            T plan = candidatePool.get(i);
            int checkIndex = planSeqList.get(planIndexMap.get(plan));
            if (checkIndex != i) {
                System.out.println("index is different");
            }

            planSeqList.set(planIndexMap.get(plan), i);
        }
        size = candidatePoolSize;

    }

    //target을 poolstart이전으로 옮긴다.
    public void remove(T _plan) {
        if (!planIndexMap.containsKey(_plan)) {
            return;
        }

        int targetIndex = planSeqList.get(planIndexMap.get(_plan));
        if (targetIndex < candidatePoolStartIndex) {
            return;
        }

        //위치를 변경하면 안된다.
        int swapIndex = getSwapIndex();

        //candidatePool swap
        Collections.swap(candidatePool, candidatePoolStartIndex, targetIndex);

        //planSeqList swap
        planSeqList.set(planIndexMap.get(_plan), candidatePoolStartIndex);
        planSeqList.set(swapIndex, targetIndex);
        candidatePoolStartIndex++;

        size--;

    }


    // target을 poolstart 이후로 옮긴다.ㅣ
    public void add (T _plan) {
        if (!planIndexMap.containsKey(_plan)){
            return;
        }

        int targetIndex = planSeqList.get(planIndexMap.get(_plan));
        if (targetIndex >= candidatePoolStartIndex) {
            return;
        }

        candidatePoolStartIndex--;

        //위치를 변경하면 안된다.
        int swapIndex = getSwapIndex();

        //candidatePool swap
        Collections.swap(candidatePool, candidatePoolStartIndex, targetIndex);

        //planSeqList swap
        planSeqList.set(planIndexMap.get(_plan), candidatePoolStartIndex);
        planSeqList.set(swapIndex, targetIndex);
        size ++;
    }

    public int getSwapIndex () {
        T swapPlan = candidatePool.get(candidatePoolStartIndex);
        int swapIndex = planIndexMap.get(swapPlan);
        return swapIndex;
    }


    public void removeAllCandi() {
        candidatePoolStartIndex = candidatePoolSize;
        size = 0;
    }

    public void restoreAllCandi() {
        candidatePoolStartIndex = 0;
        size = candidatePoolSize;
    }

    public T randomPick(Random random) {
        if (candidatePoolSize == candidatePoolStartIndex){
            return null;
        }

        int targetIndex = candidatePoolStartIndex + random.nextInt(candidatePoolSize - candidatePoolStartIndex);
        return candidatePool.get(targetIndex);
    }


    public boolean isEmpty() {
        if (candidatePoolSize == candidatePoolStartIndex) {
            return true;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public boolean contains(T _plan) {
        int index = planSeqList.get(planIndexMap.get(_plan));
        if (index < candidatePoolStartIndex) {
            return false;
        } else {
            return true;
        }
    }

    public List<Integer> getPlanSeqList() {
        return this.planSeqList;
    }

    public void setPlanSeqList(List<Integer> _planSeqList) {
        this.planSeqList = _planSeqList;
    }



    @Override
    public Iterator<T> iterator() {
        return new RandomListIterator<>();
    }


    @SuppressWarnings("hiding")
    class RandomListIterator<T> implements Iterator<T> {

        private int iteratorIndex = candidatePoolStartIndex;

        @Override
        public boolean hasNext() {
            if (iteratorIndex <candidatePoolSize) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public T next() {
            @SuppressWarnings("unchecked")
            T t = (T) candidatePool.get(iteratorIndex);
            iteratorIndex++;
            return t;


        }
    }


}
