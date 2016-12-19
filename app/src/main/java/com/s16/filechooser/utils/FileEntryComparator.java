package com.s16.filechooser.utils;

import com.s16.filechooser.FileSettings;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;

public class FileEntryComparator<T> implements Comparator<T> {

	private class Holder {
		private final T mBase;
		
		Holder(T base) {
			mBase = base;
		}
		
		boolean isNull() {
			return ((Object)mBase) == null;
		}
		
		boolean isDirectory() {
			if (isNull()) return false;
			
			try {
				Method method = mBase.getClass().getMethod("isDirectory");
				if (method != null) {
					return (boolean)method.invoke(mBase);
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return false;
		}
		
		String getName() {
			if (isNull()) return "";
			
			try {
				Method method = mBase.getClass().getMethod("getName");
				if (method != null) {
					return (String)method.invoke(mBase);
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return "";
		}

        long length() {
            if (isNull()) return -1L;
            try {
                Method method = mBase.getClass().getMethod("length");
                if (method != null) {
                    Long value = (Long)method.invoke(mBase);
                    if (value != null) {
                        return value.longValue();
                    }
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return -1L;
        }

        long lastModified() {
            if (isNull()) return -1L;
            try {
                Method method = mBase.getClass().getMethod("lastModified");
                if (method != null) {
                    Long value = (Long)method.invoke(mBase);
                    if (value != null) {
                        return value.longValue();
                    }
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return -1L;
        }
	}

    private FileSettings mSettings;

	public FileEntryComparator(FileSettings settings) {
        mSettings = settings;
    }

	@Override
	public int compare(T lhs, T rhs) {
		Holder one = new Holder(lhs);
		Holder two = new Holder(rhs);
		
		if (one.isNull() && two.isNull()) {
			return 0;
			
		} else if(!one.isNull() && two.isNull()) {
			return -1;
			
		} else if(one.isNull() && !two.isNull()) {
			return 1;
			
		} else if(one.isDirectory() && !two.isDirectory()) {
			return -1;
			
		} else if(!one.isDirectory() && two.isDirectory()) {
			return 1;
			
		} else {
            int sortBy = FileSettings.SORT_BY_NAME;
            if (mSettings != null) {
                sortBy = mSettings.getSortBy();
            }

            if (sortBy == FileSettings.SORT_BY_DATE || sortBy == FileSettings.SORT_BY_SIZE) {
                long val1;
                long val2;
                if (sortBy == FileSettings.SORT_BY_DATE) {
                    val1 = one.lastModified();
                    val2 = two.lastModified();
                } else {
                    val1 = one.length();
                    val2 = two.length();
                }

                if (val1 > val2) {
                    return 1;
                } else if (val1 < val2) {
                    return -1;
                } else {
                    return 0;
                }
            } else {
                String name1 = one.getName();
                String name2 = two.getName();

                if (name1 == null && name2 == null) {
                    return 0;
                } else if (name1 == null && name2 != null) {
                    return -1;
                } else if (name1 != null && name2 == null) {
                    return 1;
                } else {
                    return name1.compareToIgnoreCase(name2);
                }
            }
		}
	}

}
