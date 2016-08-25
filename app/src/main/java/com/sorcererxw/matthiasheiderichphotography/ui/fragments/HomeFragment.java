package com.sorcererxw.matthiasheiderichphotography.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sorcererxw.matthiasheiderichphotography.models.LibraryBean;
import com.sorcererxw.matthiasheidericphotography.R;
import com.sorcererxw.matthiasheiderichphotography.ui.views.LibraryListView;
import com.sorcererxw.matthiasheiderichphotography.util.TypefaceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sorcerer on 2016/8/22.
 */
public class HomeFragment extends Fragment {

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @BindView(R.id.textView_home_introduce)
    TextView mIntroduce;

    @BindView(R.id.libraryListView)
    LibraryListView mLibraryListView;

    @BindView(R.id.textView_home_prject)
    TextView mProject;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        mIntroduce.setTypeface(TypefaceHelper.getTypeface(getContext(), TypefaceHelper.Type.Book));
        mIntroduce.setText(Html.fromHtml(
                "A simple client of Matthias Heiderich Photography, more information on <a href='http://www.matthias-heiderich.de'>www.matthias-heiderich.de</a>"));
        mIntroduce.setMovementMethod(LinkMovementMethod.getInstance());

        mProject.setTypeface(TypefaceHelper.getTypeface(getContext(), TypefaceHelper.Type.Book));
        mProject.setText(Html.fromHtml(
                "This project address <a href='https://github.com/sorcererXW/MatthiasHeiderichPhotography'>Github</a>"));
        mProject.setMovementMethod(LinkMovementMethod.getInstance());

        for (LibraryBean aMLibraryBeen : mLibraryBeen) {
            mLibraryListView.addItem(aMLibraryBeen);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private LibraryBean[] mLibraryBeen = new LibraryBean[]{
            new LibraryBean(
                    "RxAndroid", // name
                    "ReactiveX", // author
                    "Copyright 2015 The RxAndroid authors\n"
                            + "\n"
                            + "Licensed under the Apache License, Version 2.0 (the \"License\");\n"
                            + "you may not use this file except in compliance with the License.\n"
                            + "You may obtain a copy of the License at\n"
                            + "\n"
                            + "    http://www.apache.org/licenses/LICENSE-2.0\n"
                            + "\n"
                            + "Unless required by applicable law or agreed to in writing, software\n"
                            + "distributed under the License is distributed on an \"AS IS\" BASIS,\n"
                            + "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n"
                            + "See the License for the specific language governing permissions and\n"
                            + "limitations under the License.", // licence
                    "https://github.com/ReactiveX/RxAndroid", // link
                    "RxJava bindings for Android"  // des
            ),
            new LibraryBean(
                    "RxJava", // name
                    "ReactiveX", // author
                    "Copyright 2013 Netflix, Inc.\n"
                            + "\n"
                            + "Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at\n"
                            + "\n"
                            + "http://www.apache.org/licenses/LICENSE-2.0\n"
                            + "\n"
                            + "Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.",
                    // licence
                    "https://github.com/ReactiveX/RxJava", // link
                    "RxJava – Reactive Extensions for the JVM – a library for composing asynchronous and event-based programs using observable sequences for the Java VM."
                    // des
            ),
            new LibraryBean(
                    "glide", // name
                    "bumptech", // author
                    "License for everything not in third_party and not otherwise marked:\n"
                            + "\n"
                            + "Copyright 2014 Google, Inc. All rights reserved.\n"
                            + "\n"
                            + "Redistribution and use in source and binary forms, with or without modification, are\n"
                            + "permitted provided that the following conditions are met:\n"
                            + "\n"
                            + "   1. Redistributions of source code must retain the above copyright notice, this list of\n"
                            + "         conditions and the following disclaimer.\n"
                            + "\n"
                            + "   2. Redistributions in binary form must reproduce the above copyright notice, this list\n"
                            + "         of conditions and the following disclaimer in the documentation and/or other materials\n"
                            + "         provided with the distribution.\n"
                            + "\n"
                            + "THIS SOFTWARE IS PROVIDED BY GOOGLE, INC. ``AS IS'' AND ANY EXPRESS OR IMPLIED\n"
                            + "WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND\n"
                            + "FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GOOGLE, INC. OR\n"
                            + "CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR\n"
                            + "CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR\n"
                            + "SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON\n"
                            + "ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING\n"
                            + "NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF\n"
                            + "ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\n"
                            + "\n"
                            + "The views and conclusions contained in the software and documentation are those of the\n"
                            + "authors and should not be interpreted as representing official policies, either expressed\n"
                            + "or implied, of Google, Inc.\n"
                            + "---------------------------------------------------------------------------------------------\n"
                            + "License for third_party/disklrucache:\n"
                            + "\n"
                            + "Copyright 2012 Jake Wharton\n"
                            + "Copyright 2011 The Android Open Source Project\n"
                            + "\n"
                            + "Licensed under the Apache License, Version 2.0 (the \"License\");\n"
                            + "you may not use this file except in compliance with the License.\n"
                            + "You may obtain a copy of the License at\n"
                            + "\n"
                            + "   http://www.apache.org/licenses/LICENSE-2.0\n"
                            + "\n"
                            + "Unless required by applicable law or agreed to in writing, software\n"
                            + "distributed under the License is distributed on an \"AS IS\" BASIS,\n"
                            + "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n"
                            + "See the License for the specific language governing permissions and\n"
                            + "limitations under the License.\n"
                            + "---------------------------------------------------------------------------------------------\n"
                            + "License for third_party/gif_decoder:\n"
                            + "\n"
                            + "Copyright (c) 2013 Xcellent Creations, Inc.\n"
                            + "\n"
                            + "Permission is hereby granted, free of charge, to any person obtaining\n"
                            + "a copy of this software and associated documentation files (the\n"
                            + "\"Software\"), to deal in the Software without restriction, including\n"
                            + "without limitation the rights to use, copy, modify, merge, publish,\n"
                            + "distribute, sublicense, and/or sell copies of the Software, and to\n"
                            + "permit persons to whom the Software is furnished to do so, subject to\n"
                            + "the following conditions:\n"
                            + "\n"
                            + "The above copyright notice and this permission notice shall be\n"
                            + "included in all copies or substantial portions of the Software.\n"
                            + "\n"
                            + "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND,\n"
                            + "EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF\n"
                            + "MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND\n"
                            + "NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE\n"
                            + "LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION\n"
                            + "OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION\n"
                            + "WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.\n"
                            + "---------------------------------------------------------------------------------------------\n"
                            + "License for third_party/gif_encoder/AnimatedGifEncoder.java and\n"
                            + "third_party/gif_encoder/LZWEncoder.java:\n"
                            + "\n"
                            + "No copyright asserted on the source code of this class. May be used for any\n"
                            + "purpose, however, refer to the Unisys LZW patent for restrictions on use of\n"
                            + "the associated LZWEncoder class. Please forward any corrections to\n"
                            + "kweiner@fmsware.com.\n"
                            + "\n"
                            + "-----------------------------------------------------------------------------\n"
                            + "License for third_party/gif_encoder/NeuQuant.java\n"
                            + "\n"
                            + "Copyright (c) 1994 Anthony Dekker\n"
                            + "\n"
                            + "NEUQUANT Neural-Net quantization algorithm by Anthony Dekker, 1994. See\n"
                            + "\"Kohonen neural networks for optimal colour quantization\" in \"Network:\n"
                            + "Computation in Neural Systems\" Vol. 5 (1994) pp 351-367. for a discussion of\n"
                            + "the algorithm.\n"
                            + "\n"
                            + "Any party obtaining a copy of these files from the author, directly or\n"
                            + "indirectly, is granted, free of charge, a full and unrestricted irrevocable,\n"
                            + "world-wide, paid up, royalty-free, nonexclusive right and license to deal in\n"
                            + "this software and documentation files (the \"Software\"), including without\n"
                            + "limitation the rights to use, copy, modify, merge, publish, distribute,\n"
                            + "sublicense, and/or sell copies of the Software, and to permit persons who\n"
                            + "receive copies from any such party to do so, with the only requirement being\n"
                            + "that this copyright notice remain intact.", // licence
                    "https://github.com/bumptech/glide", // link
                    "An image loading and caching library for Android focused on smooth scrolling"
                    // des
            ),
            new LibraryBean(
                    "butterknife", // name
                    "JakeWharton", // author
                    "Copyright 2013 Jake Wharton\n"
                            + "\n"
                            + "Licensed under the Apache License, Version 2.0 (the \"License\");\n"
                            + "you may not use this file except in compliance with the License.\n"
                            + "You may obtain a copy of the License at\n"
                            + "\n"
                            + "   http://www.apache.org/licenses/LICENSE-2.0\n"
                            + "\n"
                            + "Unless required by applicable law or agreed to in writing, software\n"
                            + "distributed under the License is distributed on an \"AS IS\" BASIS,\n"
                            + "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n"
                            + "See the License for the specific language governing permissions and\n"
                            + "limitations under the License.\n", // licence
                    "https://github.com/JakeWharton/butterknife", // link
                    "Bind Android views and callbacks to fields and methods."  // des
            ),
            new LibraryBean(
                    "AVLoadingIndicatorView", // name
                    "81813780", // author
                    "Copyright 2015 jack wang\n"
                            + "\n"
                            + "Licensed under the Apache License, Version 2.0 (the \"License\");\n"
                            + "you may not use this file except in compliance with the License.\n"
                            + "You may obtain a copy of the License at\n"
                            + "\n"
                            + "   http://www.apache.org/licenses/LICENSE-2.0\n"
                            + "\n"
                            + "Unless required by applicable law or agreed to in writing, software\n"
                            + "distributed under the License is distributed on an \"AS IS\" BASIS,\n"
                            + "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n"
                            + "See the License for the specific language governing permissions and\n"
                            + "limitations under the License.", // licence
                    "https://github.com/81813780/AVLoadingIndicatorView", // link
                    "Nice loading animations for Android"  // des
            ),
            new LibraryBean(
                    "Android-Iconics", // name
                    "mikepenz", // author
                    "Copyright 2016 Mike Penz\n"
                            + "\n"
                            + "Licensed under the Apache License, Version 2.0 (the \"License\");\n"
                            + "you may not use this file except in compliance with the License.\n"
                            + "You may obtain a copy of the License at\n"
                            + "\n"
                            + "   http://www.apache.org/licenses/LICENSE-2.0\n"
                            + "\n"
                            + "Unless required by applicable law or agreed to in writing, software\n"
                            + "distributed under the License is distributed on an \"AS IS\" BASIS,\n"
                            + "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n"
                            + "See the License for the specific language governing permissions and\n"
                            + "limitations under the License.", // licence
                    "https://github.com/mikepenz/Android-Iconics", // link
                    "Android-Iconics - Use any icon font, or vector (.svg) as drawable in your application."
                    // des
            ),
            new LibraryBean(
                    "MaterialDrawer",
                    "mikepenz",
                    "Copyright 2016 Mike Penz\n"
                            + "\n"
                            + "Licensed under the Apache License, Version 2.0 (the \"License\");\n"
                            + "you may not use this file except in compliance with the License.\n"
                            + "You may obtain a copy of the License at\n"
                            + "\n"
                            + "   http://www.apache.org/licenses/LICENSE-2.0\n"
                            + "\n"
                            + "Unless required by applicable law or agreed to in writing, software\n"
                            + "distributed under the License is distributed on an \"AS IS\" BASIS,\n"
                            + "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n"
                            + "See the License for the specific language governing permissions and\n"
                            + "limitations under the License.",
                    "https://github.com/mikepenz/MaterialDrawer",
                    "The flexible, easy to use, all in one drawer library for your Android project."),
            new LibraryBean(
                    "material-dialogs",
                    "afollestad",
                    "The MIT License (MIT)\n"
                            + "\n"
                            + "Copyright (c) 2014-2016 Aidan Michael Follestad\n"
                            + "\n"
                            + "Permission is hereby granted, free of charge, to any person obtaining a copy\n"
                            + "of this software and associated documentation files (the \"Software\"), to deal\n"
                            + "in the Software without restriction, including without limitation the rights\n"
                            + "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n"
                            + "copies of the Software, and to permit persons to whom the Software is\n"
                            + "furnished to do so, subject to the following conditions:\n"
                            + "\n"
                            + "The above copyright notice and this permission notice shall be included in all\n"
                            + "copies or substantial portions of the Software.\n"
                            + "\n"
                            + "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n"
                            + "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n"
                            + "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n"
                            + "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n"
                            + "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n"
                            + "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE\n"
                            + "SOFTWARE.",
                    "https://github.com/afollestad/material-dialogs",
                    "A beautiful, fluid, and customizable dialogs API. "),
            new LibraryBean(
                    "FloatingActionButton", // name
                    "Clans", // author
                    "Copyright 2015 Dmytro Tarianyk\n"
                            + "\n"
                            + "Licensed under the Apache License, Version 2.0 (the \"License\");\n"
                            + "you may not use this file except in compliance with the License.\n"
                            + "You may obtain a copy of the License at\n"
                            + "\n"
                            + "   http://www.apache.org/licenses/LICENSE-2.0\n"
                            + "\n"
                            + "Unless required by applicable law or agreed to in writing, software\n"
                            + "distributed under the License is distributed on an \"AS IS\" BASIS,\n"
                            + "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n"
                            + "See the License for the specific language governing permissions and\n"
                            + "limitations under the License.", // licence
                    "https://github.com/Clans/FloatingActionButton", // link
                    "Android Floating Action Button based on Material Design specification\n"
                    // des
            ),
            new LibraryBean(
                    "PhotoView",
                    "chrisbanes",
                    "Copyright 2011, 2012 Chris Banes\n"
                            + "\n"
                            + "Licensed under the Apache License, Version 2.0 (the \"License\");\n"
                            + "you may not use this file except in compliance with the License.\n"
                            + "You may obtain a copy of the License at\n"
                            + "\n"
                            + "   http://www.apache.org/licenses/LICENSE-2.0\n"
                            + "\n"
                            + "Unless required by applicable law or agreed to in writing, software\n"
                            + "distributed under the License is distributed on an \"AS IS\" BASIS,\n"
                            + "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n"
                            + "See the License for the specific language governing permissions and\n"
                            + "limitations under the License.",
                    "https://github.com/chrisbanes/PhotoView",
                    "Implementation of ImageView for Android that supports zooming, by various touch gestures."
            )
    };
}
