package site.patrickshao.admin.biz.utils;

import site.patrickshao.admin.biz.repository.DefaultRepository;
import site.patrickshao.admin.common.entity.PojoWithIdentifier;
import site.patrickshao.admin.common.entity.po.AbstractPersistObject;
import site.patrickshao.admin.common.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/12
 */
public class Joiner {
    public static <T> RepoQueryLeftImpl getJoiner(Long id) {
        return new RepoQueryLeftImpl(id);
    }




    public static class RepoQueryLeftImpl {
        private final List<Long> ids;

        private RepoQueryLeftImpl(Long id) {
            this.ids = new ArrayList<>();
            this.ids.add(id);
        }

        public <T extends AbstractPersistObject>
        ResultList<T> selectLeft(DefaultRepository<T> repo) {
            return new ResultList<T>(repo.selectByIds(ids));
        }
    }

    public static class ResultList<T extends PojoWithIdentifier> {
        private final List<T> list;

        public List<T> toList() {
            return list;
        }

        private ResultList(List<T> list) {
            this.list = list;
        }

        public <V extends AbstractPersistObject>
        QueriedPairList<T, V> selectRightChildren(DefaultRepository<V> repo) {

            List<Pair<T, V>> pairs = new ArrayList<>();
            for (var father : list) {
//                if (father instanceof PojoWithIdentifier pojo) {
                var children = repo.selectByParentId(father.getPojoIdentifier());
                children.stream().map(c -> new Pair<>(father, c)).forEach(pairs::add);
//                } else {
//                    throw new UnsupportedOperationException();
//                }
            }
            return new QueriedPairList<>(pairs);
        }

        public <V extends AbstractPersistObject>
        QueriedPairList<T, V> selectRightByIdFromLeft(Function<T, Long> getter, DefaultRepository<V> repo) {
            List<Pair<T, V>> pairs = new ArrayList<>();
            for (var father : list) {
                var child = repo.selectById(getter.apply(father));
                var pair = new Pair<>(father, child);
                pairs.add(pair);
            }
            return new QueriedPairList<>(pairs);
        }


    }

    public static class QueriedPairList<T extends PojoWithIdentifier, V> {
        private final List<Pair<T, V>> list;

        private QueriedPairList(List<Pair<T, V>> list) {
            this.list = list;
        }

        public <R extends PojoWithIdentifier> ResultList<R> join(BiFunction<T, V, R> joiningLambda) {
            List<R> res = list.stream()
                    .map(pair -> joiningLambda.apply(pair.getFirst(), pair.getSecond()))
                    .toList();
            return new ResultList<>(res);
        }

        public CompliedQueryPairList<T, V> compileByFirst() {
            List<Pair<T, List<V>>> ls = list.stream()
                    .collect(Collectors.groupingBy(p -> p.getFirst().getPojoIdentifier()))
                    .values()
                    .stream()
                    .map(list -> {
                        var first = list.get(0).getFirst();
                        var second = list.stream().map(Pair::getSecond).toList();
                        return new Pair<>(first, second);
                    })
                    .toList();
            return new CompliedQueryPairList<T, V>(ls);
        }


        public List<Pair<T, V>> toList() {
            return list;
        }
    }

    public static class CompliedQueryPairList<T, V> {
        private List<Pair<T, List<V>>> ls;

        public CompliedQueryPairList(List<Pair<T, List<V>>> ls) {
            this.ls = ls;
        }

        public <R> CompliedQueryPairList<T, R> mapSecondTo(Function<V, R> action) {
            var res = ls.stream().map(pair -> {
                var newSecond = pair.getSecond().stream().map(action).toList();
                return new Pair<>(pair.getFirst(), newSecond);
            }).toList();
            return new CompliedQueryPairList<>(res);
        }

        public List<Pair<T, List<V>>> toList() {
            return ls;
        }
    }
    private Joiner() {
    }

}