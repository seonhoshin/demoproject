import React from "react";
import { useParams } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

const UserProfileDetails = ({ toggleAccountSettings, profileData }) => {
  const { id } = useParams();
  const { logout } = useAuth();
  const memberId = localStorage.getItem("memberId");

  const handleLogout = async () => {
    try {
      localStorage.setItem("prevPath", window.location.pathname);
      await logout();
    } catch (error) {}
  };

  return (
    <div className="flex flex-row justify-center items-center md:flex-col md:mb-6 ">
      {profileData && (
        <>
          <img
            className="flex items-center rounded-full object-cover w-[75px] h-[75px] m-2.5 md:mt-10 md:mb-6 md:w-[120px] md:h-[120px] xl:w-[130px] xl:h-[130px]"
            src={
              profileData.profileImg
                ? profileData.profileImg
                : "https://homepagepictures.s3.ap-northeast-2.amazonaws.com/client/public/images/userImg.png"
            }
            alt="profile"
          />
          <div className="flex flex-col justify-center items-start md:items-center">
            <div className="text-md pb-1 md:text-xl font-semibold ">
              {profileData.nickname}
            </div>
            <div className=" text-xs md:text-sm pb-4 md:mx-16 md:my-4 break-all">
              {profileData.myIntro}
            </div>
            {memberId === id && (
              <div className="flex justify-between">
                <button
                  className="bg-[#00647B] text-white font-semibold hover:bg-[#00647B]/50 rounded-full p-1 md:p-2 text-xs md:text-md mr-2"
                  onClick={() => toggleAccountSettings(true)}
                >
                  계정설정
                </button>
                <button
                  className="block sm:hidden bg-[#00647B] text-white font-semibold hover:bg-[#00647B]/50 rounded-full p-1 md:p-2 text-xs md:text-md"
                  onClick={handleLogout}
                >
                  로그아웃
                </button>
              </div>
            )}
          </div>
        </>
      )}
    </div>
  );
};

export default UserProfileDetails;
