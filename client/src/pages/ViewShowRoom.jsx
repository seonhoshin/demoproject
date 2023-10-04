import React, { useRef, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { toast } from "react-hot-toast";
import useAxios from "../hooks/useAxios";
import Background from "../components/common/Background";
import ViewCoverImg from "../components/feed/view/ViewCoverImg";
import Sidebar from "../components/feed/view/Sidebar";
import ShowroomContents from "../components/feed/view/ShowroomContents";
import Edit from "../components/feed/view/Edit";
import CommentWrap from "../components/feed/comment/showroom/CommentWrap";

const ViewShowRoom = () => {
  // 받아온 API 공유하기 위한 상태
  const [feedData, setFeedData] = useState({});
  // 사이드바 댓글 이동 버튼
  const commentSectionRef = useRef(null);

  const { feedId } = useParams();
  const [response, error, loading] = useAxios({
    method: "GET",
    url: `/feed/${feedId}`,
    headers: {
      "ngrok-skip-browser-warning": "69420",
    },
  });

  // 작정자 멤버아이디 (게시글ID(feedId)와 memberId 일치시에만 On)
  const userId = response?.data?.data?.memberId;

  // 로컬에 저장된 memberID 가져오기
  const memberId = localStorage.getItem("memberId");

  useEffect(() => {
    if (response) {
      setFeedData(response.data.data);
    } else if (error) {
      console.error("Error:", error);
    }
  }, [response, error]);

  useEffect(() => {
    if (!feedData && loading) {
      toast.loading("로딩중...");
    } else if (feedData || error) {
      // toast.dismiss();
    }
  }, [feedData, loading, error]);

  return (
    <div>
      <ViewCoverImg setFeedData={setFeedData} feedData={feedData} />

      <Sidebar
        setFeedData={setFeedData}
        feedData={feedData}
        commentSectionRef={commentSectionRef}
      />
      <Background
        mainclassName="bg-[#FFFAEE] h-full px-14 md:px-56 pb-40"
        divclassName="flex-col my-24 md:my-0"
      >
        <ShowroomContents setFeedData={setFeedData} feedData={feedData} />

        {memberId == userId && <Edit feedData={feedData} />}
        <CommentWrap
          feedData={feedData}
          setFeedData={setFeedData}
          ref={commentSectionRef}
        />
      </Background>
    </div>
  );
};

export default ViewShowRoom;
