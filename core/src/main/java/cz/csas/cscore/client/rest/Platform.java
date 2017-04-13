/*
 * Copyright (C) 2013 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cz.csas.cscore.client.rest;

import android.os.Build;
import android.os.Process;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import cz.csas.cscore.CoreSDK;
import cz.csas.cscore.client.rest.android.MainThreadExecutor;
import cz.csas.cscore.client.rest.client.Client;
import cz.csas.cscore.client.rest.client.UrlConnectionClient;
import cz.csas.cscore.client.rest.converter.Converter;
import cz.csas.cscore.client.rest.converter.GsonConverter;
import cz.csas.cscore.logger.LogManager;
import cz.csas.cscore.utils.csjson.CsJson;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;
import static java.lang.Thread.MIN_PRIORITY;

abstract class Platform {
    private static final Platform PLATFORM = findPlatform();

    static Platform get() {
        return PLATFORM;
    }

    private static Platform findPlatform() {
        try {
            Class.forName("android.os.Build");
            if (Build.VERSION.SDK_INT != 0) {
                return new Android();
            }
        } catch (ClassNotFoundException ignored) {
        }
        return new Base();
    }

    abstract Converter defaultConverter();

    abstract Client.Provider defaultClient();

    abstract Executor defaultHttpExecutor();

    abstract Executor defaultCallbackExecutor();

    abstract LogManager defaultLog();

    /**
     * Provides sane defaults for operation on the JVM.
     */
    private static class Base extends Platform {
        @Override
        Converter defaultConverter() {
            return new GsonConverter(new CsJson());
        }

        @Override
        Client.Provider defaultClient() {
            final Client client;
            client = new UrlConnectionClient();
            return new Client.Provider() {
                @Override
                public Client get() {
                    return client;
                }
            };
        }

        @Override
        Executor defaultHttpExecutor() {
            return Executors.newCachedThreadPool(new ThreadFactory() {
                @Override
                public Thread newThread(final Runnable r) {
                    return new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Thread.currentThread().setPriority(MIN_PRIORITY);
                            r.run();
                        }
                    }, CsRestAdapter.IDLE_THREAD_NAME);
                }
            });
        }

        @Override
        Executor defaultCallbackExecutor() {
            return new Utils.SynchronousExecutor();
        }

        @Override
        LogManager defaultLog() {
            return CoreSDK.getInstance().getLogger();
        }
    }

    /**
     * Provides sane defaults for operation on Android.
     */
    private static class Android extends Platform {

        @Override
        Converter defaultConverter() {
            return new GsonConverter(new CsJson());
        }

        @Override
        Client.Provider defaultClient() {
            final Client client;
            client = new UrlConnectionClient();

            return new Client.Provider() {
                @Override
                public Client get() {
                    return client;
                }
            };
        }

        @Override
        Executor defaultHttpExecutor() {
            return Executors.newCachedThreadPool(new ThreadFactory() {
                @Override
                public Thread newThread(final Runnable r) {
                    return new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Process.setThreadPriority(THREAD_PRIORITY_BACKGROUND);
                            r.run();
                        }
                    }, CsRestAdapter.IDLE_THREAD_NAME);
                }
            });
        }

        @Override
        Executor defaultCallbackExecutor() {
            return new MainThreadExecutor();
        }

        @Override
        LogManager defaultLog() {
            return CoreSDK.getInstance().getLogger();
        }
    }

    /**
     * Determine whether or not OkHttp 1.6 or newer is present on the runtime classpath.
     */
    private static boolean hasOkHttpOnClasspath() {
        try {
            Class.forName("com.squareup.okhttp.OkHttpClient");
            return true;
        } catch (ClassNotFoundException ignored) {
        }
        return false;
    }
}
