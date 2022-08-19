$version = '3.6.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'B352A7D95CEC65E8A6EF5A7EEC9142B698C98AF73996A481CF0823F56054239B'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
