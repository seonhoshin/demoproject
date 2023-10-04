import React from "react";
import { Link } from "react-router-dom";

const BestInteriorHeader = () => {
  return (
    <div className="flex mt-10 justify-between items-center mx-8">
      <div className="flex items-baseline">
        <h1 className="text-3xl md:text-4xl text-[#F5634A] font-semibold Showcard-Gothic w-max">
          Best Interior
        </h1>
        <h2 className="hidden md:block pl-4 text-gray-500 font-medium text-lg">
          삐삐에서 핫한 Best 10
        </h2>
      </div>

      <Link to="/showroom/write">
        <button className="bg-[#00647B] hover:bg-[#00647bb4] shadow py-2 px-6 text-2xl font-semibold text-white rounded-md">
          Post
        </button>
      </Link>
    </div>
  );
};

export default BestInteriorHeader;
