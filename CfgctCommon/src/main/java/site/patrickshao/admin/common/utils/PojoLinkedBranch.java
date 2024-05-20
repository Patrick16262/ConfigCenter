package site.patrickshao.admin.common.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import site.patrickshao.admin.common.entity.BranchPojo;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/14
 */
@ParametersAreNonnullByDefault
public class PojoLinkedBranch<T extends BranchPojo> {
    private Atom<T> start;

    public Optional<List<T>> getPojoListToPoint(final Long destPojoId) {
        var res = new ArrayList<T>();
        if (backtraceSearch(destPojoId, start, res)) {
            return Optional.of(res);
        }
        return Optional.empty();
    }

    public static <T extends BranchPojo> PojoLinkedBranch<T> linkPojo(List<T> pojoList) {

        HashMap<Long, T> rawPojoMap = new HashMap<>();
        pojoList.forEach(p -> rawPojoMap.put(p.getPojoIdentifier(), p));
        HashMap<Long, LinkedList<Atom<T>>> branchMap = new HashMap<>();
        Atom<T> startAtom = null;

        ListMultimap<Long, Atom<T>> unsatisfiedFork = ArrayListMultimap.create();
        ListMultimap<Long, Atom<T>> unsatisfiedJoin = ArrayListMultimap.create();
        //每次循环创造一个分支
        while (!rawPojoMap.isEmpty()) { //todo
            Long pojoId = rawPojoMap.keySet().toArray(Long[]::new)[0];
            T pojo = rawPojoMap.get(pojoId);
            Long branchId = pojo.getBranchId();
            Throwables.throwOnCondition(branchMap.containsKey(branchId)).illegalArgument();

            LinkedList<Atom<T>> branch = new LinkedList<>();

            branch.add(new Atom<>(pojo));
            rawPojoMap.remove(pojo.getPojoIdentifier());
            branchMap.put(branchId, branch);

            //向左添加节点
            T curForward = pojo;
            while (curForward.getNextId() != null && curForward.getNextId().equals(branchId)) {
                curForward = rawPojoMap.get(curForward.getNextId());
                branch.addLast(new Atom<>(curForward));
                rawPojoMap.remove(curForward.getPojoIdentifier());
            }
            if (curForward.getNextId() == null) {
                branch.addLast(new Atom<>(AtomType.END));
            } else {
                Atom<T> tmp = new Atom<>(AtomType.JOIN);
                unsatisfiedJoin.put(curForward.getNextId(), tmp);
                branch.addLast(tmp);
            }

            //向右添加节点
            T curBack = pojo;
            Atom<T> curNode = new Atom<>(pojo);
            while (curBack.getPreviousId() != null && curBack.getPreviousId().equals(branchId)) {
                curBack = rawPojoMap.get(curBack.getPreviousId());
                curNode = new Atom<>(curBack);
                branch.addFirst(new Atom<>(curBack));
                rawPojoMap.remove(curForward.getPojoIdentifier());
            }
            if (curBack.getPreviousId() == null) {
                Throwables.throwOnCondition(startAtom != null).illegalArgument();
                startAtom = new Atom<>(AtomType.START);
                branch.addFirst(startAtom);
            } else {
                unsatisfiedFork.put(curBack.getPreviousId(), curNode);
            }

            for (var atom : branch) {
                atom.branchId = branchId;
                if (atom.type != AtomType.NODE) {
                    continue;
                }
                Long curId = atom.content.getPojoIdentifier();
                unsatisfiedFork.get(curId).forEach(a -> {
                            var gen = new Atom<T>(AtomType.FORK);
                            gen.forkNext = atom;
//                            gen.forkBranchId = atom.content.getBranchId();
                            branch.add(branch.indexOf(atom), gen);
                        }
                );
                unsatisfiedFork.removeAll(curId);
                unsatisfiedJoin.get(curId).forEach(a -> {
                    var gen = new Atom<T>(AtomType.JOIN_POINT);
                    branch.add(branch.indexOf(atom), gen);
                    a.toJoinPoint = gen;
                });
                unsatisfiedJoin.removeAll(curId);
            }
        }

        PojoLinkedBranch<T> res = new PojoLinkedBranch<T>();
        res.start = startAtom;
        return res;
    }

    private boolean backtraceSearch(final Long pojoId, final Atom<T> cur, final List<T> variableResList) {
        switch (cur.type) {
            case FORK -> {
                if (backtraceSearch(pojoId, cur.forkNext, variableResList)) {
                    return true;
                }
                if (backtraceSearch(pojoId, cur.next, variableResList)) {
                    return true;
                }
            }
            case NODE -> {
                variableResList.add(cur.content);
                if (cur.content.getPojoIdentifier().equals(pojoId)) {
                    return true;
                }
                if (backtraceSearch(pojoId, cur.next, variableResList)) {
                    return true;
                }
                variableResList.remove(cur.content);
            }
            case JOIN, END -> {
                return false;
            }
            case JOIN_POINT, START -> {
                return backtraceSearch(pojoId, cur.next, variableResList);
            }
        }
        return false;
    }

    private PojoLinkedBranch() {
    }

    private static class Atom<T> {
        AtomType type;
        Atom<T> next;
        Atom<T> forkNext;
        //        Long forkBranchId;
        Atom<T> toJoinPoint;
        T content;
        Long branchId;

        public Atom(T content) {
            this.content = content;
            this.type = AtomType.NODE;
        }

        public Atom(AtomType type) {

        }

        @Override
        public String toString() {
            return "Atom{" +
                    "type=" + type +
                    ", next=" + next +
                    ", forkNext=" + forkNext +
                    ", toJoinPoint=" + toJoinPoint +
                    ", content=" + content +
                    '}';
        }
    }

    private enum AtomType {
        FORK,
        NODE,
        JOIN,
        JOIN_POINT,
        END,
        START,
    }
}
