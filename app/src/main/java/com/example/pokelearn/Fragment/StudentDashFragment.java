package com.example.pokelearn.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.pokelearn.Activities.Discuss;
import com.example.pokelearn.Activities.Home;
import com.example.pokelearn.Activities.JoinQuiz;
import com.example.pokelearn.Activities.S_MyCourse;
import com.example.pokelearn.Activities.S_Progress;
import com.example.pokelearn.R;

public class StudentDashFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private CardView sMyCourseCard, sDiscuss, sProgressCard, sExploreCard, sJoinQuizCard;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public StudentDashFragment() {

    }

    public static StudentDashFragment newInstance(String param1, String param2) {
        StudentDashFragment fragment = new StudentDashFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_student_dash, container, false);

        sMyCourseCard = (CardView) v.findViewById(R.id.s_myCourses_card);
        sDiscuss = (CardView) v.findViewById(R.id.s_discuss_card);
        sProgressCard = (CardView) v.findViewById(R.id.s_progress_card);
        sExploreCard = (CardView) v.findViewById(R.id.s_explore_card);
        sJoinQuizCard = (CardView) v.findViewById(R.id.s_joinQuiz_card);

        sMyCourseCard.setOnClickListener(this);
        sDiscuss.setOnClickListener(this);
        sProgressCard.setOnClickListener(this);
        sExploreCard.setOnClickListener(this);
        sJoinQuizCard.setOnClickListener(this);

        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.s_myCourses_card:
                i = new Intent(getActivity(), S_MyCourse.class); startActivity(i); break;
            case R.id.s_discuss_card:
                i = new Intent(getActivity(), Discuss.class );startActivity(i);  break;
            case R.id.s_progress_card:
                i = new Intent(getActivity(), S_Progress.class );startActivity(i); break;
            case R.id.s_explore_card:
                ((Home) getActivity())
                        .setActionBarTitle("Home");
                getFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();break;
            case R.id.s_joinQuiz_card:
                i = new Intent(getActivity(), JoinQuiz.class );startActivity(i);  break;
            default:
                break;


        }
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
