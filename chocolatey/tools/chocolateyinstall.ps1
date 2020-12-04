$version = '2.2.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '833120C1580FD8F81CAEB47BC946C72F3D1866123FFA5B63B4BC4BA21DFFFEA8'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
