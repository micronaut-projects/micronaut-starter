$version = '5.2.8'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '2C68C29A4DA486815AB9B36A0EF945A6A5128AF2D1ADE298AEB3763F046875F5'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
