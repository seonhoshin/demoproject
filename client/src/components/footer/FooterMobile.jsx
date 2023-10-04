import React from "react";
import { Link } from "react-router-dom";

const liPoint = "flex flex-col items-center w-12";

const FooterMobile = () => {
  const memberId = localStorage.getItem("memberId");

  return (
    <footer>
      <div className="flex justify-center border py-4 fixed bottom-0 w-full bg-white z-50">
        <ul className="flex items-center justify-around w-11/12 text-gray-400 font-medium">
          {/* 쇼룸 */}
          <Link to="/showroom">
            <li className={liPoint}>
              <img
                src="https://homepagepictures.s3.ap-northeast-2.amazonaws.com/client/public/images/Showroom.png"
                alt="쇼룸"
              />
              <span className="w-24 text-center mt-2">Show room</span>
            </li>
          </Link>

          {/* 팁 */}
          <Link to="/tips">
            <li className={liPoint}>
              <img
                src="https://homepagepictures.s3.ap-northeast-2.amazonaws.com/client/public/images/Tips.png"
                alt="팁"
              />
              <span className="mt-2">Tips</span>
            </li>
          </Link>

          {/* 홈 */}
          <Link to="/">
            <li>
              <img
                className="w-14"
                src="https://homepagepictures.s3.ap-northeast-2.amazonaws.com/client/public/images/logo.png"
                alt="홈"
              />
            </li>
          </Link>

          {/* 맵 */}
          <Link to="/map">
            <li className={liPoint}>
              <img
                src="https://homepagepictures.s3.ap-northeast-2.amazonaws.com/client/public/images/map.png"
                alt="맵"
              />
              <span className="mt-2">Map</span>
            </li>
          </Link>

          {/* 유저 (로그인 여부에 따라 경로 달라져야 함) */}
          <Link to={memberId ? `/myinfo/${memberId}` : "/login"}>
            <li className={liPoint}>
              <img
                src="https://homepagepictures.s3.ap-northeast-2.amazonaws.com/client/public/images/user.png"
                alt="유저"
              />
              <span className="mt-2">My</span>
            </li>
          </Link>
        </ul>
      </div>
    </footer>
  );
};

export default FooterMobile;
