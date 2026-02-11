$version = '4.10.8'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '979DA386A11CAF6C19D7C98C9277DFA39D0F280DB981469587B5C1B0AC1F2001'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
