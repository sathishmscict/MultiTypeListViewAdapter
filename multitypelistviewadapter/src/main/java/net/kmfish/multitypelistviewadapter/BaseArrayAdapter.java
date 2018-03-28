package net.kmfish.multitypelistviewadapter;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lijun on 16/8/5.
 */
public class BaseArrayAdapter implements IArrayAdapter {

    public static final String TAG = BaseArrayAdapter.class.getSimpleName();

    private boolean mNotifyOnChange = true;
    @NonNull
    private final Object mLock = new Object();

    @NonNull
    private final List<Data> datas = new ArrayList<>();

    @NonNull
    private final ListItemTypeHelper helper = new ListItemTypeHelper();
    private NotifyDataSetChangedListener listener;

    BaseArrayAdapter(NotifyDataSetChangedListener listener) {
        this.listener = listener;
    }

    @Override
    public void addData(Data item) {
        synchronized (mLock) {
            datas.add(item);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    @Override
    public void addDatas(List<Data> items) {
        synchronized (mLock) {
            this.datas.addAll(items);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    @Override
    public void addDatas(Data... items) {
        synchronized (mLock) {
            Collections.addAll(this.datas, items);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    @Override
    public void insert(Data item, int index) {
        synchronized (mLock) {
            datas.add(index, item);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    @Override
    public void remove(Data object) {
        synchronized (mLock) {
            if (datas.contains(object)) {
                datas.remove(object);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    @Override
    public void remove(int pos) {
        synchronized (mLock) {
            datas.remove(pos);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    @Override
    public void clear() {
        synchronized (mLock) {
            datas.clear();
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    @Override
    public List<Data> getDatas() {
        return Collections.unmodifiableList(datas);
    }

    @Override
    public void notifyDataSetChanged() {
        mNotifyOnChange = true;
        if (null != listener) {
            listener.notifyDataSetChanged();
        }
    }

    @Override
    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public int getItemType(int position) {
        Data item = getData(position);
        if (null != item) {
            return getItemType(item);
        }

        return -1;
    }

    @Override
    public Data getData(int position) {
        if (position >= 0 && position < datas.size()) {
            return datas.get(position);
        }

        return null;
    }

    Class<?> getDataClass(int viewType) {
        return helper.getItemClass(viewType);
    }


    private int getItemType(Data data) {
        if (null != data && null != data.getData()) {
            return helper.getType(data.getData().getClass());
        }

        return -1;
    }
}
